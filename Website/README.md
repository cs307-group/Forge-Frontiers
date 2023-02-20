# Forge frontier Web Site and Server

This directory is part of the main Forge Frontier monorepo.

This directory contains code for the Next.js website and Flask server that deals with the Forge frontiers web ui.

**frontend: ** Contains the next.js with typescript code

**backend: ** Contains a flask app that supports jwt with a json api

## Architecture

The app will be a next.js frontend with server rendering using data fetching from the flask json api

Authentication will be done using httponly first party cookies

## Database

We will use a manged postgres database, for now you can setup a local postgres database using docker. Check the backend readme for more info
