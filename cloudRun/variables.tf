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