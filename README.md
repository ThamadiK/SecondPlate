# Event App — simple MVC boilerplate

## Run
```bash
docker compose up -d
./mvnw spring-boot:run
```
App runs on `localhost:8080`.

## Structure
```
model/       -> User, Event, Booking (the "M" in MVC — plain fields, matches the ER diagram)
repository/  -> one JpaRepository interface per entity (auto-implemented by Spring, no code needed)
service/     -> business logic goes here (one class per entity, thin for now)
controller/  -> the "C" in MVC — HTTP endpoints, delegate straight to service
```
There's no explicit "View" folder — this is a JSON API, so Spring converts
returned objects to JSON automatically. That conversion is the "V".

## Endpoints (all three follow the same pattern)
- `GET  /api/events`      — list all
- `GET  /api/events/{id}` — get one
- `POST /api/events`      — create

Same shape for `/api/users` and `/api/bookings`.

## Where things go from here
- Add a rule like "don't allow booking past capacity"? → `BookingService.createBooking`.
- Add "don't show past events"? → `EventService.getAllEvents`.
- Controllers should stay boring: parse the request, call the service, return the result.
