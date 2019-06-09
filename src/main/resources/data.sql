delete
from car_brand;
TRUNCATE TABLE car_brand;

--biedaków nie obsługujemy xD
insert into car_brand(brand_name)
values ('Default'),
       ('Bugatti'),
       ('Ferrari'),
       ('Maserati'),
       ('Maybach');

delete
from car_parts;
TRUNCATE TABLE car_parts;
insert into car_parts(name, tax, producer)
values ('Silnik', 18,'Ursus'),
       ('Chłodnica - taka lodówka przenośna do samochodu', 10, 'Amica'),
       ('Tarcze hamulcowe, ew. broń rzucana w razie napadu', 11, 'Boschki producent'),
       ('Skonczyły mi się pomysły', 12, 'Producent');

delete
from car_service_data;
TRUNCATE TABLE car_service_data;

insert into car_service_data (apartment_number, building_number, city_name, company_name, NIP, street_name, zip_code,
                              email)
values ('', '11a', 'Miasto', 'Złomek-Warsztat Samochodowy', '123-456-78-19', 'Ulica', '20-123', 'mail@mail.pl');

delete
from cars;
TRUNCATE TABLE cars;

insert into cars (model, prod_year, vin_number, brand_id) VALUES
('Default', 2000, '11345678901112132',1),
('Veyron', 2003, '12345678901112131',2),
('FXX', 2006, '11345678901112131',3);

delete
from clients;
TRUNCATE TABLE clients;

insert into clients (created_at, email, first_name, last_logged_in, last_name, password, apartment_number, build_number,
                     city_name, phone_number, status, street_name, zip_code)
values (NOW(), 'car@warsztatZlomek.pl', 'Default', NOW(), 'Default', '$2a$10$vfgXB9jlN9AB74LaLW96ju6AfO0F0CUiAyOvmu7wZsyeLvU2n58l.',
        '', '11', 'Default', '123456789', 0, 'Default', '11-111'),
        (NOW(), 'mail@mail.pl', 'Jan', NOW(), 'Kowalski', '$2a$10$vfgXB9jlN9AB74LaLW96ju6AfO0F0CUiAyOvmu7wZsyeLvU2n58l.',
        '', '12', 'Miasto', '123456789', 0, 'Ulica', '12-123');
delete
from cars_has_owners;
TRUNCATE TABLE cars_has_owners;

insert into cars_has_owners (car_pk, owner_pk, begin_ownership_date, end_ownership_date, registration_number, status,
                             car_id, owner_id)
values (1,1, NOW(), null, 'LU-1231', 0, 1,1), (2,1, '2018-02-02', NOW(), 'LU-1232', 0, 2, 1);

delete
from client_token;
TRUNCATE TABLE client_token;

delete
from companies;
TRUNCATE TABLE companies;

insert into companies (apartment_number, building_number, city_name, company_name, NIP, street_name, zip_code, email)
VALUES ('', '123', 'Miasto', 'Firma', '123-123-12-14', 'Ulica', '12-321', 'jakis@mail.pl');

delete
from companies_has_employees;
TRUNCATE TABLE companies_has_employees;

delete
from employee_token;
TRUNCATE TABLE employee_token;

delete
from overviews;
TRUNCATE TABLE overviews;

insert into overviews (overview_date, overview_last_day, car_id) VALUES
( timestamp '2019-01-02 10:00',  timestamp '2020-01-02 10:00', 1);

delete
from services;
TRUNCATE TABLE services;

insert into services (name, tax) VALUES ('Schłodzenie browarka w chłodnicy', 11),
                                        ('Rzut tarczą na piastę', 12);
delete
from visits;
TRUNCATE TABLE visits;

insert into visits (created_at, status, updated_at, visit_date, visit_finished, car_id, client_id, employee_id,
                    overview_id)
values (NOW(),  0, NOW(), timestamp '2019-07-07 11:00', null , 2, 1,1, null),
       (timestamp '2019-01-01 21:12',  4, timestamp '2019-01-09 13:30', timestamp '2019-01-02 10:00', timestamp '2019-01-02 10:00' , 1,1,1, 1);

delete
from visit_has_services;
TRUNCATE TABLE visit_has_services;

delete
from visits_has_parts;
TRUNCATE TABLE visits_has_parts;