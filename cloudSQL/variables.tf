variable "region" {
  type        = string
  description = "GCP project default region"
}

variable "zone" {
  type        = string
  description = "GCP project default zone"
}

variable "cloud_sql_db_instance_name" {
  type        = string
  description = "Name of cloud sql db instance"
}

variable "db_name" {
  type = string
}

variable "db_user" {
  type = string
}

variable "db_pass" {
  type = string
}