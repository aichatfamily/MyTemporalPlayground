# Temporal Playground (Java)

This project provides hands-on examples to help you learn and experiment with [Temporal](https://temporal.io/), a durable execution platform. It includes a basic "Hello, World" greeting workflow and a more advanced web crawling workflow.

## Prerequisites

Before you begin, ensure you have the following installed:

1.  **Java 21+**: This project is built with Java 21. You can verify your version with `java --version`.
2.  **Temporal CLI**: The `temporal` command-line tool is required to run a local Temporal server. Follow the [official instructions](https://docs.temporal.io/cli#install) to install it.

## Getting Started

Follow these steps to get the project running.

### 1. Start the Temporal Server

First, start a local development server. The server will continue running until you stop it (`Ctrl+C`).

```bash
# In a dedicated terminal window
temporal server start-dev
```

### 2. Build the Project

Next, compile the Java source code using the included Gradle wrapper.

```bash
./gradlew build
```

This command downloads dependencies and builds the project. You only need to run it once.

## Example 1: "Hello, World" Greeting

This simple workflow demonstrates the basics of a client, a worker, and a workflow execution.

### Step 1: Run the Worker

The worker hosts the workflow and activity code. It polls a specific task queue, waiting for work.

```bash
# In a new terminal window
./gradlew run -PmainClass=org.example.application.WorkerApp
```

You should see output indicating the worker has started:
`Worker started for task queue: GREETING_TASK_QUEUE`

### Step 2: Run the Client

The client starts the workflow and waits for its result.

```bash
# In a third terminal window
./gradlew run -PmainClass=org.example.application.ClientApp
```

The client will print the workflow's return value and then exit:
`[main] INFO org.example.application.ClientApp - Greeting result: Hello, World`

## Example 2: Web Crawler

This example demonstrates a more complex workflow that crawls a website, processes its `robots.txt`, and generates output files in different formats (JSON, CSV, XML).

### Step 1: Run the Web Crawl Worker

This worker is responsible for executing the web crawling logic.

```bash
# In a new terminal window
./gradlew run -PmainClass=org.example.application.WebCrawlWorkerApp
```

### Step 2: Run the Web Crawl Client

The client initiates three different web crawl workflows with various output formats.

```bash
# In another terminal window
./gradlew run -PmainClass=org.example.application.WebCrawlClientApp
```

After running, you will find the generated output files (e.g., `yahoo_com_output.json`, `google_com_output.csv`) in the project's root directory.

## Project Structure

The project is organized into two main examples:

-   `src/main/java/org/example/greeting`: Contains the simple "Hello, World" workflow.
-   `src/main/java/org/example/webcrawl`: Contains the web crawling workflow, activities, and data models.
-   `src/main/java/org/example/application`: Contains the client and worker application entry points.

Feel free to explore the code to learn more about how Temporal works!
