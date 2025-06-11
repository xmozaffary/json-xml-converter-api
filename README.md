# JSON/XML Converter API

## Beskrivning

Detta är en REST API som erbjuder funktionalitet för att konvertera mellan JSON och XML-format. API:et stödjer både direkt konvertering av data och konvertering av data från externa URL:er. Det är byggt med Spring Boot och använder Jackson för datahantering.

## Komma igång

### Förutsättningar

- Java 21
- Maven
- Docker

### Installation och körning

1. Klona projektet:

```bash
git clone https://github.com/xmozaffary/json-xml-converter-api.git
cd json-xml-converter-api
```

2. Bygg projektet med Maven:


```bash
./mvnw clean install
```

3. Starta applikationen:

```bash
./mvnw spring-boot:run
```

Applikationen kommer att starta på :http://localhost:8080

### Docker

För att köra applikationen i Docker:

1. Bygg Docker-imagen:

```bash
docker build -t json-xml-converter-api .
```

2. Kör containern:

```bash
docker run -p 8080:8080 json-xml-converter-api
```

## API Dokumentation

- Swagger är bara för dokumentation, för test ska man använda request.http
- Swagger UI: http://localhost:8080/swagger-ui/index.html

## API Endpoints

------------------------------
### POST /api/convert/json-to-xml

Konverterar JSON till XML.

**Request Body:**

```json
{
  "name": "John Doe",
  "age": 30,
  "city": "Stockholm"
}
```

**Response:**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<item>
    <name>John Doe</name>
    <age>30</age>
    <city>Stockholm</city>
</item>
```
-------------------------
### POST /api/convert/xml-to-json

Konverterar XML till JSON.

**Request Body:**

```xml
<item>
    <name>John Doe</name>
    <age>30</age>
    <city>Stockholm</city>
</item>
```

**Response:**

```json
{
  "name": "John Doe",
  "age": "30",
  "city": "Stockholm"
}
```
-----------------------
### POST /api/convert/fetch-json-to-xml

Hämtar JSON från en extern URL och konverterar det till XML.

**Request Body:**

```json
{
  "url": "https://jsonplaceholder.typicode.com/users"
}
```
**Response:** XML-representation av den hämtade JSON-datan.

-----------------------------
### POST /api/convert/fetch-xml-to-json

Hämtar XML från en extern URL och konverterar det till JSON.

**Request Body:**

```json
{
  "url": "https://www.w3schools.com/xml/note.xml"
}
```

**Response:** JSON-representation av den hämtade XML-datan.

------------------------
#### Bra för att testa stora xml datamängder

```json
{
  "url": "https://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml"
}
```
----------------------


## Testning

Se `requests.http` för färdiga test-requests. Du kan använda dessa requests direkt i IntelliJ IDEA.

Exempel på test-requests:

```http
### Konvertera JSON till XML
POST http://localhost:8080/api/convert/json-to-xml
Content-Type: application/json

{
    "name": "John Doe",
    "age": 30,
    "city": "Stockholm"
}

### Konvertera XML till JSON
POST http://localhost:8080/api/convert/xml-to-json
Content-Type: application/xml

<item>
    <name>John Doe</name>
    <age>30</age>
    <city>Stockholm</city>
</item>
```

## Reflektioner

### Arkitektur och Designval

1. **projekt stukur**

    - Implementerade en service-baserad arkitektur för bättre separation av ansvar

2. **API Design**

    - Separerade endpoints för direkt konvertering och externa källor
    - Använde tydliga URL-strukturer för bättre förståelse
    - Implementerade konsekvent felhantering över alla endpoints

3. **Docker Implementation**
    - Valde Amazon Corretto som JDK för dess stabilitet och AWS-optimering
    - Implementerade en enkel men effektiv Dockerfile

### Utmaningar och Lösningar

1. **Hantering av Extern Data**

    - **Utmaning:** Hantering av olika format från externa källor
    - **Lösning:** Försökte implementerade robust felhantering och validering
    - **Resultat:** Försökte skapa ett stabilt system som kan hantera olika datakällor


3. **Felhantering**
    - **Utmaning:** Hantera olika typer av fel på ett konsekvent sätt
    - **Lösning:** Implementerade en global exception handler
    - **Resultat:** Tydliga felmeddelanden och konsekvent felhantering

### Framtida Förbättringar

3. **Säkerhet**
    - Rate limiting
    - Input-validering

Jag fått mycket hjälp av AI vid implementationen av Swagger/OpenAPI-dokumentation med 
annotationer som `@Operation` och `@ApiResponse`.
