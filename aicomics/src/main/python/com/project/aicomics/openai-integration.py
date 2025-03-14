from dotenv import load_dotenv
import os
from openai import OpenAI

load_dotenv()  # Load environment variables
api_key = os.getenv("OPENAI_API_KEY")

client = OpenAI(
    api_key=api_key
)

response = client.responses.create(
    model="gpt-4o-mini",
    instructions="You are a coding assistant that talks like a pirate.",
    input="How do I check if a Python object is an instance of a class?",
)

print(response.output_text)