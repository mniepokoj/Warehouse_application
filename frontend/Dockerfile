# Używamy obrazu bazowego z Node.js
FROM node

# Ustalamy katalog roboczy w kontenerze
WORKDIR /app

# Kopiujemy pliki aplikacji do kontenera
COPY warehouse-app .

# Instalujemy zależności
RUN npm install

# Budujemy aplikację
RUN npm run build

# Określamy port, na którym działa aplikacja
EXPOSE 3000

CMD [ "npm", "start" ]