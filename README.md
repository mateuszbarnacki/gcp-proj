# Serverless todo list

W ramach projektu planowane jest stworzenie architektury serverless dla aplikacji służącej do planowania rzeczy do zrobienia. Funkcjonalności aplikacji będą udostępnione za pomocą Cloud Run. Dane będą przechowywane w Cloud Datastore, lub w przypadku zaimplementowania mechanizmu autoryzacji w Cloud SQL. Po dodaniu nowego obiektu użytkownik będzie otrzymywał e-mail z informacją o zaplanowanej aktywności. 

W dalszej części można stworzyć Cloud Scheduler, który na oddzielny topic wysyłałby event służący do wysyłania przypomnień. Przypomnienia byłyby osobną opcją zaznaczaną przez użytkowników. Serwis do przypomnień wysyłałby powiadomienia dzień wcześniej o określonej porze. Mechanizm wysyłania powiadomień opierałby się na usłudze Cloud Scheduler.

![alt text](https://github.com/mateuszbarnacki/gcp-proj/blob/main/ServerlessTodo.png)
