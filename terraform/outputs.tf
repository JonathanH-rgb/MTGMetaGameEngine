output "ecr_repository_url" {
  description = "Registry URL"
  value       = aws_ecr_repository.app.repository_url
}
