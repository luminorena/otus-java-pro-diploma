insert into users (id, fio, email)
VALUES ('918761b7-78a6-455f-b66a-2c9d82a856b7', 'Иванов Олег Васильевич', 'ivanov@test.ru'),
       ('b6325d2b-1fb3-48ee-819c-70aa76e3f8f5', 'Крапивица Мария Сергеевна', 'krapiva@test.ru'),
       ('6fbe91c5-8a69-4e64-b9e1-c8506572dad9', 'Харитонов Федор Маркович', 'fedor_mark@test.ru'),
       ('208ae6c2-a51e-4a86-85a5-95cd4f04ef60', 'Николаева Алефтина Юрьевна', 'jurok_10@test.ru'),
       ('9c93cba2-7508-4694-91b1-c9c4488f6c1d', 'Петрова Анна Николаевна', 'annushka_00@test.ru');

insert into updates(id, operation_system_area, description)
VALUES ('517d969f-9655-4699-af6a-fcf86dc1da59', '.NET',
        'Cumulative Update for .NET Framework 3.5, 4.8 and 4.8.1 for Windows 10 Version 22H2'),
       ('0d13a28d-5cb2-4699-a9b1-16e4f365a4b3', 'WSL2', 'Windows Subsystem for Linux Update 5.10.102.2'),
       ('c71c1d04-03c8-4b08-8d8f-48bd8d9d099a', 'Windows',
        'Cumulative Update for Windows 10 Version 22H2 for x64-based systems'),
       ('dbeef044-e349-4568-98fd-dcc32b0907c9', 'Driver', 'HP - USB'),
       ('af2bc992-6409-436d-9c8a-4bc821638ec3', 'Driver', 'Intel Firmware');

insert into user_updates(id, user_id, update_id)
VALUES ('84dbe625-4514-4819-9ff1-df004c274683', '918761b7-78a6-455f-b66a-2c9d82a856b7',
        '517d969f-9655-4699-af6a-fcf86dc1da59'),
       ('cac9bc6b-82cb-412b-9c65-8424a9fa70bc', 'b6325d2b-1fb3-48ee-819c-70aa76e3f8f5',
        'c71c1d04-03c8-4b08-8d8f-48bd8d9d099a'),
       ('543a12a3-b353-4556-acc5-0ae3681a0f18', '6fbe91c5-8a69-4e64-b9e1-c8506572dad9',
        'c71c1d04-03c8-4b08-8d8f-48bd8d9d099a'),
       ('76fe5040-a3df-47a1-b1ab-7d9de83546f0', '208ae6c2-a51e-4a86-85a5-95cd4f04ef60',
        'dbeef044-e349-4568-98fd-dcc32b0907c9'),
       ('d9590a18-b9e0-4803-9771-2dc2c13ef0e5', '9c93cba2-7508-4694-91b1-c9c4488f6c1d',
        'af2bc992-6409-436d-9c8a-4bc821638ec3');




