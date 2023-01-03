variable "project_id" {
  type        = string
  description = "GCP project identifier"
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