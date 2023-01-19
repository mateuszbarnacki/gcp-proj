resource "google_sql_database_instance" "db_instance" {
  name             = var.cloud_sql_db_instance_name
  region           = var.region
  database_version = "POSTGRES_14"

  settings {
    tier = "db-f1-micro"

    location_preference {
      zone = var.zone
    }
  }
}

resource "google_sql_database" "db" {
  name      = var.db_name
  instance  = google_sql_database_instance.db_instance.name
  charset   = "utf8"
  depends_on = [google_sql_database_instance.db_instance]
}

resource "google_sql_user" "users" {
  name     = var.db_user
  instance = google_sql_database_instance.db_instance.name
  password = var.db_pass
  depends_on = [google_sql_database.db]
}