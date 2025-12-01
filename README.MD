# ⚡ Armandillo: Cross-Platform Runtime Lua Scripting Engine for Minecraft

[![GitHub License](https://img.shields.io/badge/License-All%20Rights%20Reserved-red.svg?style=flat-square)](LICENSE)  
[![GitHub Stars](https://img.shields.io/github/stars/username/armandillo?style=flat-square)](https://github.com/username/armandillo/stargazers)  
[![Discord](https://img.shields.io/discord/your-discord-id?label=Discord&style=flat-square)](your-discord-invite-link)

---

## 📖 Overview

**Armandillo** is a runtime scripting engine built specifically for the Minecraft modding environment. It empowers modpack creators to dynamically manipulate game logic, content, and events using the lightweight and fast **Lua** scripting language via the **LuaJ** implementation.

Built on top of the **Architectury API**, Armandillo supports both **Fabric** and **NeoForge** modding platforms, ensuring seamless compatibility across them. Creators can centralize modifications — such as dynamic registration of items, blocks, custom recipes, and advanced event handling — inside external Lua script files, making development modular, flexible, and easy to manage.

With minimal performance overhead and powerful customization capabilities, Armandillo enables modpacks to come alive dynamically and efficiently.

---

## ✨ Key Features

- Native cross-platform support for **Fabric** and **NeoForge** via Architectury API
- Lua scripting execution powered by the fast and lightweight **LuaJ** engine
- Dynamic registration of **Items**, **Blocks**, and **Recipes** via Lua scripts
- Full event handling: player actions, world generation, tick events, and more
- Modular system for easy extension with new modules and custom functions
- Runtime loading and execution of scripts without recompiling the mod

---

## 🚀 Getting Started (For Modpack Creators)

### Installation

1. Install **Fabric Loader** or **NeoForge** in your Minecraft setup.
2. Download the **Armandillo** mod from [Modrinth](modrinth-link) or [CurseForge](curseforge-link).
3. Place the `.jar` file into your Minecraft `mods` folder.

### Script Structure

Place your Lua scripts inside the default modpack folder at:  
`<minecraft-folder>/armandillo/scripts/`

Scripts will be automatically loaded at game startup.