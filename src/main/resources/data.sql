-- Runs automatically on every app startup
-- The DELETE first means restarting the app resets to a clean, known set of dummy rows instead of piling up duplicates every restart.
DELETE FROM events;

INSERT INTO events (title, organizer_id, location, event_date, event_time, capacity, recurring, frequency, ticket_price, description, cuisine_type, dietary_tags, time_of_day, image_url)
VALUES
('Cooking Class by Chef Phillip', 1, 'Fitzroy, Melbourne', '2026-09-14', '18:00:00', 12, FALSE, NULL, 45.00, 'Learn to make fresh pasta from scratch', 'Italian', 'Vegetarian', 'Evening', NULL),
('Sri Lankan Food Meetup', 2, 'Dandenong, Melbourne', '2026-09-15', '12:30:00', 8, FALSE, NULL, 20.00, 'Home-style rice and curry, shared table', 'Sri Lankan', 'Gluten-Free', 'Afternoon', NULL),
('Indonesian Rijsttafel Night', 3, 'Richmond, Melbourne', '2026-09-16', '19:00:00', 10, FALSE, NULL, 35.00, 'A full spread of Indonesian classics', 'Indonesian', 'Gluten-Free', 'Evening', NULL),
('Sunday Brunch Club', 1, 'St Kilda, Melbourne', '2026-09-20', '10:00:00', 6, TRUE, 'Weekly', 15.00, 'Casual brunch and coffee for newcomers', 'Australian', 'Vegan', 'Morning', NULL),
('Dumpling Folding Workshop', 4, 'Box Hill, Melbourne', '2026-09-21', '17:30:00', 10, FALSE, NULL, 30.00, 'Hands-on dumpling making, eat what you make', 'Chinese', NULL, 'Evening', NULL),
('Moroccan Tagine Table', 2, 'Brunswick, Melbourne', '2026-09-22', '18:30:00', 8, TRUE, 'Fortnightly', 25.00, 'Slow-cooked tagine and mint tea', 'Moroccan', 'Halal', 'Evening', NULL);