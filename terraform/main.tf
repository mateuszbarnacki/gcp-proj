terraform {
    required_providers {
        google = {
            source = "hashicorp/google"
        }
    }
}

locals {
    project = var.project_id
}

provider "google" {
    version = "4.47.0"
    project = local.project
    region  = "europe-west3"
    zone    = "europe-west3-b"
}

resource "google_project_service" "pubsub" {
    service            = "pubsub.googleapis.com"
    disable_on_destroy = false
}

// PubSub

resource "google_pubsub_topic" "pubsub-topic" {
    name = "confirmation-topic"
    message_storage_policy {
        allowed_persistence_regions = [
            "europe-west3",
        ]
    }
    message_retention_duration = "86400s"
}

resource "google_project_service" "function" {
    service            = "cloudfunctions.googleapis.com"
    disable_on_destroy = false
}

// Cloud Function Gen 2

resource "google_cloudfunctions2_function" "function" {
    name        = "confirmation-handler"
    location    = "europe-west3"
    description = "This function sends email when user create new todo item"

    build_config {
        runtime    = "nodejs16"
        entryPoint = "confirmationHandler"
        source {
            repo_source {
                branch_name = "main"
                dir         = "cloud-functions/confirmationHandler"
                project_id  = local.project
                repo_name   = "mateuszbarnacki/gcp-proj"
            }
        }
    }

    service_config {
        min_instance_count = 1
        max_instance_count = 5
        available_memory   = "256M"

        environment_variables {
            MAIL_USERNAME_TEST = var.mail_username_test
        }

        secret_environment_variables {
            key        = "MAIL_USERNAME"
            project_id = local.project
            secret     = google_secret_manager_secret.mail-username-secret.secret_id
            version    = "latest"
        }

        secret_environment_variables {
            key        = "MAIL_PASSWORD"
            project_id = local.project
            secret     = google_secret_manager_secret.mail-password-secret.secret_id
            version    = "latest"
        }

        secret_environment_variables {
            key        = "OAUTH_CLIENTID"
            project_id = local.project
            secret     = google_secret_manager_secret.oauth-client_id-secret.secret_id
            version    = "latest"
        }

        secret_environment_variables {
            key        = "OAUTH_CLIENT_SECRET"
            project_id = local.project
            secret     = google_secret_manager_secret.oauth-client_secret-secret.secret_id
            version    = "latest"
        }

        secret_environment_variables {
            key        = "OAUTH_REFRESH_TOKEN"
            project_id = local.project
            secret     = google_secret_manager_secret.oauth-refresh-token-secret.secret_id
            version    = "latest"
        }

        secret_environment_variables {
            key        = "OAUTH_ACCESS_TOKEN"
            project_id = local.project
            secret     = google_secret_manager_secret.oauth-access-token-secret.secret_id
            version    = "latest"
        }
    }

    event_trigger {
        trigger_region = "europe-west3"
        event_type     = "google.cloud.pubsub.topic.v2.messagePublished"
        pubsub_topic   = google_pubsub_topic.pubsub-topic.id
        retry_policy   = "RETRY_POLICY_RETRY"
    }

    depends_on = [
        google_secret_manager_secret_version.mail-username-secret-ver,
        google_secret_manager_secret_version.mail-password-secret-ver,
        google_secret_manager_secret_version.oauth-client_id-secret-ver,
        google_secret_manager_secret_version.oauth-client_secret-secret-ver,
        google_secret_manager_secret_version.oauth-refresh-token-secret-ver,
        google_secret_manager_secret_version.oauth-access-token-secret-ver
    ]
}

// Secret Manager

resource "google_project_service" "secret_manager" {
    service            = "secretmanager.googleapis.com"
    disable_on_destroy = false
}

resource "google_secret_manager_secret" "mail-username-secret" {
    secret_id = "mail-username"

    replication {
        user_managed {
            replicas {
                location = "europe-west3"
            }
        }
    }
}

resource "google_secret_manager_secret" "mail-password-secret" {
    secret_id = "mail-password"

    replication {
        user_managed {
            replicas {
                location = "europe-west3"
            }
        }
    }
}

resource "google_secret_manager_secret" "oauth-client_id-secret" {
    secret_id = "oauth-client-id"

    replication {
        user_managed {
            replicas {
                location = "europe-west3"
            }
        }
    }
}

resource "google_secret_manager_secret" "oauth-client_secret-secret" {
    secret_id = "oauth-client-secret"

    replication {
        user_managed {
            replicas {
                location = "europe-west3"
            }
        }
    }
}

resource "google_secret_manager_secret" "oauth-refresh-token-secret" {
    secret_id = "oauth-refresh-token"

    replication {
        user_managed {
            replicas {
                location = "europe-west3"
            }
        }
    }
}

resource "google_secret_manager_secret" "oauth-access-token-secret" {
    secret_id = "oauth-access-token"

    replication {
        user_managed {
            replicas {
                location = "europe-west3"
            }
        }
    }
}

resource "google_secret_manager_secret_version" "mail-username-secret-ver" {
    secret      = google_secret_manager_secret.mail-username-secret.name
    secret_data = var.mail_username
    enabled     = true
}

resource "google_secret_manager_secret_version" "mail-password-secret-ver" {
    secret      = google_secret_manager_secret.mail-password-secret.name
    secret_data = var.mail_password
    enabled     = true
}

resource "google_secret_manager_secret_version" "oauth-client_id-secret-ver" {
    secret      = google_secret_manager_secret.oauth-client_id-secret.name
    secret_data = var.oauth_clientId
    enabled     = true
}

resource "google_secret_manager_secret_version" "oauth-client_secret-secret-ver" {
    secret      = google_secret_manager_secret.oauth-client_secret-secret.name
    secret_data = var.oauth_client_secret
    enabled     = true
}

resource "google_secret_manager_secret_version" "oauth-refresh-token-secret-ver" {
    secret      = google_secret_manager_secret.oauth-refresh-token-secret.name
    secret_data = var.oauth_refresh_token
    enabled     = true
}

resource "google_secret_manager_secret_version" "oauth-access-token-secret-ver" {
    secret      = google_secret_manager_secret.oauth-access-token-secret.name
    secret_data = var.oauth_access_token
    enabled     = true
}

// Cloud Run

resource "google_project_service" "run" {
    service            = "run.googleapis.com"
    disable_on_destroy = false
}

data "google_container_registry_image" "project-app" {
    name = var.image_name
}

resource "google_cloud_run_service" "server" {
    name     = "gcp-proj-app"
    location = "europe-west3"

    template {
        spec {
            containers {
                image = data.google_container_registry_image.project-app.image_url
            }
        }
    }

    metadata {
        annotations = {
            "run.googleapis.com/cloudsql-instances" = google_sql_database_instance.db_instance.connection_name
        }
    }

    traffic {
        percent         = 100
        latest_revision = true
    }
}

// Cloud SQL

resource "google_sql_database_instance" "db_instance" {
    name             = "gcp-proj-instance"
    region           = "europe-west3"
    database_version = "POSTGRES_14"

    settings {
        tier = "db-f1-micro"

        location_preference {
            zone = "europe-west3"
        }
    }

    deletion_protection = "false"
}