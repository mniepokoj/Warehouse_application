# Używamy obrazu bazowego z zainstalowaną Javą
FROM openjdk:17-oracle
# Ustalamy katalog roboczy w kontenerze
WORKDIR /app

# Kopiujemy plik JAR aplikacji do kontenera
COPY zti_project/out/artifacts/ZTI_project.jar app.jar

# Określamy port, na którym działa aplikacja
EXPOSE 8086

# Uruchamiamy aplikację po uruchomieniu kontenera
CMD ["java", "-jar", "app.jar"]