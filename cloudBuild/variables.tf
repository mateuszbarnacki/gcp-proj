variable "cloud_run_repo_name" {
  type        = string
  description = "Name of Cloud Run source repository"
}

variable "branch_name" {
  type = string
}

variable "cloud_build_filename" {
  type        = string
  description = "Name of cloud build configuration file"
}