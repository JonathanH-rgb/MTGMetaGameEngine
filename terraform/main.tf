provider "aws" {
  region = var.aws_region

  default_tags {
    tags = {
      Project   = var.project_name
      ManagedBy = "terraform"
    }
  }
}

# ECR — private container registry.
resource "aws_ecr_repository" "app" {
  name = var.project_name

  # Lets `terraform destroy` remove the repo even if it still holds images.
  force_delete = true

  image_scanning_configuration {
    scan_on_push = true # free vulnerability scan
  }
}
