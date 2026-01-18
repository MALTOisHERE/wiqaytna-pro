# Database Setup Options

This project supports multiple database options. Choose the one that works best for your environment.

## Quick Start (Recommended): H2 In-Memory Database

**No installation required!** Perfect for development and testing.

```bash
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=h2
```

**Access H2 Web Console:**
1. Open browser: http://localhost:8080/h2-console
2. JDBC URL: `jdbc:h2:mem:wiqaytna_db`
3. Username: `sa`
4. Password: (leave blank)
5. Click "Connect"

**Pros:**
- No installation needed
- Fast startup
- Perfect for testing

**Cons:**
- Data is lost when application stops
- Not for production use

---

## Option 1: PostgreSQL (Original Database)

### A. Install PostgreSQL Locally

1. Download PostgreSQL from https://www.postgresql.org/download/
2. Install and set password
3. Create database:
```bash
psql -U postgres
CREATE DATABASE wiqaytna_db;
\q
```

4. Run the application:
```bash
cd backend
mvn spring-boot:run
```

### B. Use Docker (No Installation)

```bash
# Start PostgreSQL container
docker run --name wiqaytna-postgres \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=wiqaytna_db \
  -p 5433:5432 \
  -d postgres:13

# Run application
cd backend
mvn spring-boot:run

# Stop container when done
docker stop wiqaytna-postgres

# Start again later
docker start wiqaytna-postgres
```

---

## Option 2: MySQL

If you already have MySQL installed:

1. Create database:
```bash
mysql -u root -p
CREATE DATABASE wiqaytna_db;
exit;
```

2. Update `backend/.env`:
```properties
DB_HOST=localhost
DB_PORT=3306
DB_NAME=wiqaytna_db
DB_USERNAME=root
DB_PASSWORD=your_mysql_password
```

3. Run with MySQL profile:
```bash
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

---

## Switching Between Databases

The project now supports multiple database profiles:

**Default (PostgreSQL):**
```bash
mvn spring-boot:run
```

**H2 In-Memory:**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=h2
```

**MySQL:**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

---

## Loading Sample Data

### PostgreSQL
```bash
psql -U postgres -d wiqaytna_db -f database/schema.sql
psql -U postgres -d wiqaytna_db -f database/sample-data.sql
```

### H2
Sample data won't persist in H2 (in-memory). The schema is auto-created from JPA entities.
You'll need to register users through the application UI.

### MySQL
```bash
mysql -u root -p wiqaytna_db < database/schema.sql
mysql -u root -p wiqaytna_db < database/sample-data.sql
```

Note: You may need to modify the SQL scripts slightly for MySQL compatibility (PostgreSQL-specific syntax like SERIAL vs AUTO_INCREMENT).

---

## Test Credentials (if sample data loaded)

**Doctor:**
- Email: `dr.amrani@wiqaytna.ma`
- Password: `password123`

**Patient:**
- Email: `ahmed.idrissi@gmail.com`
- Password: `password123`

---

## Troubleshooting

### H2 Console Not Opening
- Make sure you're using the h2 profile: `-Dspring-boot.run.profiles=h2`
- Check that the application started successfully
- Try: http://localhost:8080/h2-console

### Connection Refused
- Verify database is running (PostgreSQL service or Docker container)
- Check port numbers in `.env` file match your database port
- PostgreSQL default: 5432, MySQL default: 3306

### Schema Issues
- For development, use `JPA_DDL_AUTO=update` in `.env`
- For production, use `JPA_DDL_AUTO=validate` and manage schema manually

---

## Recommendation

**For Development:** Use H2 (no installation, instant setup)
**For Testing:** Use Docker PostgreSQL (production-like environment)
**For Production:** Use managed PostgreSQL service (AWS RDS, Azure Database, etc.)
