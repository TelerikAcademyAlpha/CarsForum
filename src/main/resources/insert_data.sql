INSERT INTO tags (id, name) VALUES
(1, 'mercedes'),
(2, 'tuning'),
(3, 'maintenance'),
(4, 'ev'),
(5, 'classics');


INSERT INTO users (id, first_name, last_name, email, username, password, phone_number, role, blocked, created_at) VALUES
(1, 'Ivan', 'Petrov', 'ivan.petrov@example.com', 'ivanp', '$2a$10$hashForIvan1Unique', '0888123456', 'USER', 0, '2026-01-10'),
(2, 'Mira', 'Georgieva', 'mira.georgieva@example.com', 'mirag', '$2a$10$hashForMira2Uniq', '0888234567', 'USER', 0, '2026-02-05'),
(3, 'Peter', 'Ivanov', 'peter.ivanov@example.com', 'peti', '$2a$10$hashForPeter3Uni', NULL, 'USER', 0, '2026-03-12'),
(4, 'Anna', 'Dimitrova', 'anna.dimitrova@example.com', 'annad', '$2a$10$hashForAnna4Unq', '0888345678', 'ADMIN', 0, '2026-04-20'),
(5, 'Georgi', 'Markov', 'georgi.markov@example.com', 'georgim', '$2a$10$hashForGeorgi5U', '0888456789', 'USER', 0, '2026-05-15');


INSERT INTO posts (id, title, content, author_id, created_at, updated_at) VALUES
(1, 'Tuning tips for the Mercedes C-Class', 'Best bolt-on upgrades for C-Class: intake, exhaust, remap and suspension tweaks.', 1, '2026-06-01', '2026-06-01'),
(2, 'EV charging at home: what you need to know', 'Guide to home chargers, wallbox options and required electrical upgrades.', 2, '2026-06-03', '2026-06-03'),
(3, 'Routine maintenance checklist for older cars', 'Monthly and yearly checks: fluids, brakes, belts, and corrosion prevention.', 3, '2026-06-05', '2026-06-05'),
(4, 'How to spot a good classic car purchase', 'Key things to inspect: chassis rust, matching numbers, service history and paintwork.', 4, '2026-06-07', '2026-06-07'),
(5, 'Top 5 aftermarket parts for engine performance', 'Affordable parts that give real gains: cold air intake, headers, ECU tune, exhaust.', 5, '2026-06-09', '2026-06-09');


INSERT INTO comments (id, content, author_id, post_id, created_at, updated_at) VALUES
(1, 'Tried the intake + remap on my W205 — felt a real difference in mid-range torque.', 2, 1, '2026-06-02', '2026-06-02'),
(2, 'How much will an electrician usually charge for a home charger install?', 3, 2, '2026-06-04', '2026-06-04'),
(3, 'Don''t forget to check the timing belt service interval on older engines.', 1, 3, '2026-06-06', '2026-06-06'),
(4, 'Best tip: get a lift inspection to check underside before buying a classic.', 5, 4, '2026-06-08', '2026-06-08'),
(5, 'Which ECU tune providers do you recommend for a safe stage 1 remap?', 4, 5, '2026-06-10', '2026-06-10');


INSERT INTO post_likes (id, post_id, user_id) VALUES
(1, 1, 1),
(2, 2, 2),
(3, 3, 3),
(4, 4, 4),
(5, 5, 5);


INSERT INTO post_tags (post_id, tag_id) VALUES
(1, 1),
(1, 2),
(2, 4),
(3, 3),
(4, 5);


INSERT INTO post_tags (post_id, tag_id) VALUES
(5, 2),
(5, 3);