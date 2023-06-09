DROP TABLE IF EXISTS weather_data;

CREATE TABLE weather_data (
   id                       BIGINT      AUTO_INCREMENT  PRIMARY KEY,
   client_ip                VARCHAR(50) NOT NULL,
   req_date                 DATE        NOT NULL,
   ip2loc_req_success       BOOLEAN     NOT NULL,
   weather_api_req_success  BOOLEAN     NOT NULL,
   city                     VARCHAR(50),
   latitude                 REAL,
   longitude                REAL,
   country                  VARCHAR(50),
   last_updated             DATE,
   temp_c                   REAL,
   temp_f                   REAL,
   is_day                   BOOLEAN,
   condition                VARCHAR(50),
   wind_speed_miles         REAL,
   wind_speed_km            REAL,
   wind_degree              INTEGER,
   wind_direction           VARCHAR(50),
   pressure_mb              REAL,
   pressure_in              REAL,
   precipitation_ml         REAL,
   precipitation_in         REAL,
   humidity                 INTEGER,
   cloud                    INTEGER,
   feels_like_c             REAL,
   feels_like_f             REAL,
   vis_km                   REAL,
   vis_miles                REAL,
   ultraviolet              REAL,
   gust_miles               REAL,
   gust_km                  REAL
);