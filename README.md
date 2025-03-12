# Application Overview
The project is a Java desktop application, used for creating and managing izvajalci (performers), prostori (spaces and venues) and dogodki (events) for each user (organizator).

# Database
The database was created in Toad Data Modeler. Here is the diagram:
![image](https://github.com/user-attachments/assets/13a15c02-6ae5-4120-8c41-3cfba3b7c2e3)

# Database Functions

This repository contains PostgreSQL functions for managing organizers, venues, and events in a database.

## Table of Contents
- [Login](#login)
- [Organizers](#organizers)
- [Venues (Prostori)](#venues-prostori)
- [Performers (Izvajalci)](#performers-izvajalci)
- [Places (Kraji)](#places-kraji)
- [Triggers](#triggers)

---

## Login
### `login(email TEXT, password TEXT) RETURNS INT`
Authenticates an organizer by verifying email and password.

- **Input:** Email, Password
- **Output:** Organizer ID (if credentials match)

---

## Organizers
### `getName(id_organizatorja INT) RETURNS TEXT`
Fetches the name of an organizer.

### `getOrganizers(id_organizatorja INT) RETURNS RECORD`
Retrieves organizer details.

### `update_organizator(id_organizatorja INT, ime TEXT, priimek TEXT, geslo TEXT, email TEXT) RETURNS VOID`
Updates organizer details.

### `create_user(ime TEXT, priimek TEXT, geslo TEXT, email TEXT) RETURNS VOID`
Creates a new organizer and corresponding settings entry.

### `getFont(id_settings INT) RETURNS TEXT`
Retrieves font settings of an organizer.

---

## Venues (Prostori)
### `getProstori() RETURNS SETOF RECORD`
Retrieves all venues.

### `getProstor(id_prostora INT) RETURNS RECORD`
Fetches a specific venue by ID.

### `insertProstor(id_kraja INT, naziv TEXT, kapaciteta INT) RETURNS VOID`
Adds a new venue.

### `updateProstor(id_prostora INT, naziv TEXT, kapaciteta INT) RETURNS VOID`
Updates venue details.

### `delProstor(id_prostora INT) RETURNS VOID`
Deletes a venue.

---

## Performers (Izvajalci)
### `getIzvajalci() RETURNS SETOF RECORD`
Retrieves all performers.

### `get_izvajalec(id_izvajalca INT) RETURNS RECORD`
Fetches details of a specific performer.

### `insert_izvajalec(ime TEXT, priimek TEXT, opis TEXT) RETURNS VOID`
Adds a new performer.

### `update_izvajalec(id_izvajalca INT, ime TEXT, priimek TEXT, opis TEXT) RETURNS VOID`
Updates performer details.

---

## Places (Kraji)
### `getKraji() RETURNS SETOF RECORD`
Retrieves all places.

### `getKraj(id_kraja INT) RETURNS RECORD`
Fetches details of a specific place.

---

## Triggers
### `update_st_dogodkov()`
Updates event count (`st_dogodkov`) in related tables when events (`dogodki`) are inserted, updated, or deleted.

### `dogodek_del()`
Handles event deletions and ensures consistency in related tables.

---

## License
MIT License


