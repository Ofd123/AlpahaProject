package com.example.newalpha.GeminiRelevant;

public class Prompts {
    public static final String GET_DATA_FROM_SITE =
            "You are a Product Data Extractor AI.\n" +
                    "I will provide the HTML content of a webpage.\n" +
                    "Your goal is to retrieve product information from the given content.\n" +
                    "\n" +
                    "HTML CONTENT:\n" +
                    "%s\n" +  // <--- Inject the page text/HTML here
                    "\n" +
                    "INSTRUCTIONS:\n" +
                    "1. Identify the Product Name, Price (with currency), Model/SKU, Seller, and Location.\n" +
                    "2. If multiple products exist, pick the product that seems to be the main one.\n" +
                    "3. Format the output as a strict JSON object.\n" +
                    "\n" +
                    "REQUIRED JSON STRUCTURE:\n" +
                    "{\n" +
                    "  \"status\": \"success\",\n" +
                    "  \"product_name\": \"String\",\n" +
                    "  \"price\": \"Number or String\",\n" +
                    "  \"currency\": \"String\",\n" +
                    "  \"sku\": \"String or null\",\n" +
                    "  \"seller\": \"String\",\n" +
                    "  \"location\": \"String or null\"\n" +
                    "}\n" +
                    "\n" +
                    "CONSTRAINT: Return ONLY the JSON object. Do NOT include markdown formatting or explanations.";


    public static final String GET_DATA_FROM_IMAGE = "Act as an **Expert Product Researcher and Visual Analyst**, capable of identifying and detailing any item (including footwear, electronics, furniture, machinery, or art).\n" +
            "\n" +
            "Analyze the central item in the attached image thoroughly. Provide the most in-depth report possible, covering technical specifications, history, and functional purpose.\n" +
            "\n" +
            "Please structure your report clearly using the following mandatory sections:\n" +
            "\n" +
            "1.  **Precise Identification:**\n" +
            "    * **Brand & Manufacturer:** (e.g., Nike, Sony, Herman Miller)\n" +
            "    * **Exact Model Name/Number:** (Provide the specific name and/or series number)\n" +
            "    * **Estimated Release Year/Era:** (When was this product introduced?)\n" +
            "\n" +
            "2.  **Aesthetics and Materials:**\n" +
            "    * **Primary Materials:** (e.g., specific plastics, type of leather, metal composition)\n" +
            "    * **Colorway:** (Specify the official color name if known, or a detailed description)\n" +
            "    * **Design Language:** (Is it minimalist, industrial, retro, etc.?)\n" +
            "\n" +
            "3.  **Functionality and Key Specifications:**\n" +
            "    * **Primary Use Case:** (What is its core purpose? e.g., Ultra-Marathon Running, Studio Monitoring, Gaming Peripheral)\n" +
            "    * **Unique Features/Technology:** (Patented technologies, sensor specs, processor/battery details, special mechanisms)\n" +
            "\n" +
            "4.  **Market Context and Value:**\n" +
            "    * **Rarity/Collectibility:** (Is this a rare item, a common one, or a collector's piece?)\n" +
            "    * **Estimated Market Value:** (Current retail price or typical resale/used price estimate)\n" +
            "\n" +
            "If any specific data point is uncertain, state your best educated guess based on the visual evidence provided.";
    public static final String GET_DATA_FROM_FILE = "You will now analyze a PDF file and extract structured data from it in JSON format.\n" +
            "\n" +
            "PHASE 1 — PDF ACCESS CHECK\n" +
            "1. Attempt to load the PDF exactly from the provided URL.\n" +
            "2. If the PDF cannot be accessed, cannot be rendered, or returns no readable text, your entire response must ONLY be:\n" +
            "\"Unable to access PDF.\"\n" +
            "Do NOT continue to PHASE 2 if the PDF was not loaded successfully.\n" +
            "\n" +
            "PHASE 2 — RAW PDF TEXT EXTRACTION (MANDATORY)\n" +
            "Before analyzing the product, output the following section:\n" +
            "\"=== RAW PDF SNAPSHOT ===\"\n" +
            "\n" +
            "Then show ONLY:\n" +
            "- the raw visible text extracted from the PDF\n" +
            "- no analysis\n" +
            "- no interpretation\n" +
            "- no hallucination\n" +
            "- no inferred content\n" +
            "\n" +
            "If the visible PDF text is empty or missing, stop and output:\n" +
            "\"Unable to access PDF.\"\n" +
            "\n" +
            "PHASE 3 — PRODUCT DATA EXTRACTION\n" +
            "Using ONLY the RAW PDF SNAPSHOT above, extract **strictly** the following fields into JSON:\n" +
            "\n" +
            "{\n" +
            "  \"product_name\": \"\",\n" +
            "  \"price\": \"\",\n" +
            "  \"summary\": \"\",\n" +
            "  \"specifications\": \"\"\n" +
            "}\n" +
            "\n" +
            "RULES:\n" +
            "- If a field cannot be found in the RAW PDF SNAPSHOT, its value must be exactly: \"Data not found in PDF\"\n" +
            "- Do NOT use external knowledge or assumptions.\n" +
            "- Do NOT add or guess information not explicitly present.\n" +
            "- Do NOT translate unless the PDF contains translated text.\n" +
            "- Use only the exact text visible in the RAW PDF SNAPSHOT.\n" +
            "\n" +
            "PHASE 4 — OUTPUT FORMAT (MANDATORY)\n" +
            "Output ONLY the final JSON object.\n" +
            "No commentary.\n" +
            "No markdown.\n" +
            "No surrounding text.\n" +
            "\n" +
            "BEGIN NOW.\n" +
            "Analyze the following PDF:\n";
}