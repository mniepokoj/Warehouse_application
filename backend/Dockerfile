# Używamy obrazu bazowego z zainstalowaną Javą
FROM amazoncorretto:11
# Ustalamy katalog roboczy w kontenerze
WORKDIR /app

# Kopiujemy plik JAR aplikacji do kontenera
COPY zti_project/target/ZTI_project.jar app.jar

# Określamy port, na którym działa aplikacja
EXPOSE 8086

# Uruchamiamy aplikację po uruchomieniu kontenera
CMD ["java", "-jar", "app.jar"]