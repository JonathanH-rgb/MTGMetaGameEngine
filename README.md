# MTG Metagame Engine

A Java/Spring Boot backend for ingesting Magic: The Gathering card & deck data and
answering metagame questions (co-occurrence, "decks like this also run…"). Built to be
production-shaped: containerized, deployed to AWS via Terraform, shipped by CI/CD.

## Tech stack

| Layer | Choice |
|---|---|
| Language / framework | Java 25, Spring Boot 4.1 |
| Build | Maven (via the committed `./mvnw` wrapper) |
| Container | Docker |
| Cloud | AWS |
| IaC | Terraform |
| CI/CD | GitHub Actions |
| Database | PostgreSQL |

---

## Requirements

- **JDK 25** (`java -version`)
- **Podman** or **Docker**
- **Terraform** ≥ 1.6
- **AWS CLI** 
- **AWS account **
---

## Local development

```bash
# Run the app (hot restart)
./mvnw spring-boot:run
curl http://localhost:8080/actuator/health

# Compile + run tests
./mvnw clean test

# Build & run as a container
podman build -t metagame-engine:local .
podman run --rm -p 8080:8080 metagame-engine:local
```

---

## Cloud setup — rebuild from scratch

Prerequisites: `aws sts get-caller-identity` works, Terraform installed, AWS account. Set `github_repository` in `terraform/github_oidc.tf` first.

```bash
cd terraform
terraform init

# 1. Prerequisites: registry + CI auth (everything the pipeline needs, expect a warning, but these are requirements for the app to run, can't be deployed with the app)
terraform apply \
  -target=aws_ecr_repository.app \
  -target=aws_iam_openid_connect_provider.github \
  -target=aws_iam_role.github_actions \
  -target=aws_iam_role_policy.ecr_push

# 2. Set GitHub repo variable AWS_ROLE_ARN to this value
terraform output -raw github_actions_role_arn

# 3. Push to main → CI builds & pushes the image to ECR
git push

# 4. The app: App Runner (image now exists)
terraform apply

# Live URL
terraform output -raw app_url
```

### Teardown

```bash
terraform destroy                                   # full teardown
aws apprunner pause-service --service-arn <arn>     # or just pause compute
```

---

## Project layout

```
src/                     Spring Boot app
Dockerfile               Multi-stage build (JDK build stage → JRE runtime)
terraform/               ECR, GitHub OIDC + CI role, App Runner
.github/workflows/ci.yml Test → build → push image (OIDC, no stored keys)
```
