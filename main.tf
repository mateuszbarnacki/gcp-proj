module "source_repo" {
    source              = "./sourceRepo"
    cloud_run_repo_name = var.cloud_run_repo_name
}

module "cloud_build" {
    source               = "./cloudBuild"
    depends_on           = [module.source_repo]
    cloud_run_repo_name  = var.cloud_run_repo_name
    branch_name          = var.branch_name
    cloud_build_filename = var.cloud_build_filename
}

module "cloud_run" {
    source                     = "./cloudRun"
    depends_on                 = [module.cloud_build]
    region                     = var.region
    zone                       = var.zone
    image_name                 = var.image_name
    cloud_run_name             = var.cloud_run_name
    cloud_sql_db_instance_name = var.cloud_sql_db_instance_name
}

module "cloud_function" {
    source                     = "./cloudFunction"
    depends_on                 = [module.cloud_run]
    project_id                 = var.project_id
    region                     = var.region
    pubsub_topic_name          = var.pubsub_topic_name
    bucket_name                = var.bucket_name
    bucket_object_name         = var.bucket_object_name
    cloud_function_source      = var.cloud_function_source
    cloud_function_name        = var.cloud_function_name
    cloud_function_entry_point = var.cloud_function_entry_point
    mail_username              = var.mail_username
    mail_password              = var.mail_password
    oauth_clientId             = var.oauth_clientId
    oauth_client_secret        = var.oauth_client_secret
    oauth_refresh_token        = var.oauth_refresh_token
    oauth_access_token         = var.oauth_access_token
    mail_username_test         = var.mail_username_test
}