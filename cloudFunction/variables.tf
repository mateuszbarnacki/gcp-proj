variable "project_id" {
  type        = string
  description = "GCP project identifier"
}

variable "region" {
  type        = string
  description = "GCP project default region"
}

variable "pubsub_topic_name" {
  type = string
}

variable "bucket_name" {
  type        = string
  description = "Unique cloud bucket name"
}

variable "bucket_object_name" {
  type = string
}

variable "cloud_function_source" {
  type        = string
  description = "Name of ZIP file which is stored in cloud bucket"
}

variable "cloud_function_name" {
  type        = string
  description = "Cloud function name in GCP"
}

variable "cloud_function_entry_point" {
  type        = string
  description = "Cloud function name in code"
}

variable "account_id" {
  type        = string
  description = "Cloud function service account identifier"
}

variable "display_name" {
  type        = string
  description = "Cloud function service account display name"
}

variable "mail_username" {
  type        = string
  description = "Project email address"
}

variable "mail_password" {
  type        = string
  description = "Project email password"
}

variable "oauth_clientId" {
  type        = string
  description = "OAuth client identifier for gmail API"
}

variable "oauth_client_secret" {
  type        = string
  description = "OAuth client secret for gmail api"
}

variable "oauth_refresh_token" {
  type        = string
  description = "OAuth refresh token for gmail api"
}

variable "oauth_access_token" {
  type        = string
  description = "OAuth access token for gmail api"
}

variable "mail_username_test" {
  type        = string
  description = "Email address which is used in cloud function test"
}