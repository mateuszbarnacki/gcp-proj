variable "project_id" {
  type        = string
  description = "GCP project identifier"
}

variable "project_number" {
  type        = string
  description = "GCP project number"
}

variable "cloud_run_repo_name" {
  type        = string
  description = "Name of github repository"
}

variable "branch_name" {
  type = string
}