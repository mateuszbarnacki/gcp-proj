variable "region" {
  type        = string
  description = "GCP project default region"
}

variable "image_name" {
  type        = string
  description = "Name of the Cloud Run image"
}

variable "cloud_run_name" {
  type        = string
  description = "Name of cloud run instance"
}