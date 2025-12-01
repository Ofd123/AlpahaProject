package com.example.newalpha.GeminiRelevant;

public class Prompts {
    public static final String GET_DATA_FROM_SITE = "Act as a professional product data extractor. Analyze the main product for sale at the provided URL. Please extract and list the following details: Brand, Model Name, Specific Color, Dimensions/Sizes, Material, Price, and a brief Description. If any specific detail is missing, explicitly state 'Not specified'. Please extract the data ";
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
}