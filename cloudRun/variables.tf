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

variable "region" {
  type        = string
  description = "GCP project default region"
}

variable "zone" {
  type        = string
  description = "GCP project default zone"
}

variable "image_name" {
  type        = string
  description = "Name of the Cloud Run image"
}

variable "cloud_run_name" {
  type        = string
  description = "Name of cloud run instance"
}

variable "cloud_sql_db_instance_name" {
  type        = string
  description = "Name of cloud sql db instance"
}