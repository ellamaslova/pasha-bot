
create table records (
    kinder_garden_visit boolean,
    snot boolean,
    temperature boolean,
    today date
);

INSERT INTO records (kinder_garden_visit, snot, temperature, today) VALUES
    (true,  true,  false, '2025-01-01'),
    (false, false, false, '2025-01-02'),
    (false, true,  true,  '2025-01-03'),
    (true,  true,  false, '2025-04-09'),
    (true,  false, false, '2025-04-11'),
    (true,  true,  false, '2025-04-14'),
    (false, true,  false, '2025-04-17'),
    (true,  true,  false, '2025-04-18'),
    (true,  false, false, '2025-04-21'),
    (true,  false, false, '2025-04-22');
