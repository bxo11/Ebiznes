import discord
import requests
from discord.ext import commands

TOKEN = 'MTA0NzYwNTI4ODkzNDU3NjE3OA.GqcaKr.muTocVAJzqngaH3TNc3l75TiIUgBCGfGfbpqJI'
bot = commands.Bot(command_prefix='.', help_command=None, intents=discord.Intents.all())
backend_url = 'http://localhost:8080'


@bot.command(name='categories')
async def category_list(ctx):
    url = f'{backend_url}/category'
    response = requests.get(url).json()
    output = '\n'.join(
        [f'{idx + 1}. {category["name"]} - {category["description"]}' for idx, category in enumerate(response)])
    await ctx.send(output)


@bot.command(name='new-category')
async def new_category(ctx, name, description):
    url = f'{backend_url}/category'
    category_data = {
        "name": name,
        "description": description,
    }
    response = requests.post(url, json=category_data)

    output = f'Error: {response.status_code}'
    if response.status_code == 200:
        output = "Category added successfully"
    await ctx.send(output)


@bot.command(name='products')
async def products_by_category(ctx, category):
    url = f'{backend_url}/productByCategory/{category}'
    response = requests.get(url).json()
    output = '\n'.join([f'{idx + 1}. {product["name"]}' for idx, product in enumerate(response)])
    await ctx.send(output)


@bot.command(name='new-product')
async def new_product(ctx, name, category):
    url = f'{backend_url}/product'
    product_data = {
        "name": name,
        "category": category,
    }
    response = requests.post(url, json=product_data)

    output = f'Error: {response.status_code}'
    if response.status_code == 200:
        output = "Product added successfully"
    await ctx.send(output)


@bot.event
async def on_message(message):
    if message.author == bot.user:
        return
    url = f'{backend_url}/message'
    message_data = {
        "userId": message.author.id,
        "message": message.content
    }

    requests.post(url, json=message_data)
    await bot.process_commands(message)


if __name__ == '__main__':
    bot.run(TOKEN)
