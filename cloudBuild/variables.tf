variable "project_id" {
  type        = string
  description = "GCP project identifier"
}

variable "cloud_run_repo_owner" {
  type        = string
  description = "Owner of github repository"
}

variable "cloud_run_repo_name" {
  type        = string
  description = "Name of github repository"
}

variable "branch_name" {
  type = string
}

variable "cloud_build_filename" {
  type        = string
  description = "Name of cloud build configuration file"
}