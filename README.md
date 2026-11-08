# Better Trim Tooltips — Modern Port for Minecraft 26.3 (Fabric)

![Mod loader: Fabric](https://img.shields.io/badge/modloader-Fabric-1976d2?style=for-the-badge)
![Minecraft: 26.3](https://img.shields.io/badge/Minecraft-26.3-green?style=for-the-badge)
![GitHub license](https://img.shields.io/github/license/Sekai0NI0itamio/BetterTrimTooltips263?style=for-the-badge)

**Condenses trimmed armor tooltips from three lines into one.**

Vanilla spreads an armor trim across multiple tooltip lines — the pattern,
the material, and the smithing-template upgrade text. This mod condenses
them into a single clean line:

```
Sentry (Redstone)
```

...instead of three separate lines. Easier to read, easier to scan your
inventory, and it works with Java Edition's current trim system.

Now with a **modern port for Minecraft 26.3 (Fabric)**, rebuilt on
Fabric's `ItemTooltipCallback` API for 26.3's trim data components
(`DataComponents.TRIM`, `equipment.trim.ArmorTrim`) — no fragile mixins.

### What it does

- Removes the vanilla trim pattern + upgrade tooltip lines
- Adds one condensed `Pattern (Material)` line, styled like vanilla
- StackedTrims-style multi-trim handling is preserved in behaviour

### Modern port notes (26.3)

- Old `ArmorTrim` mixin replaced with a tooltip callback — cleaner and
  more robust against future API changes
- Java 25 · Fabric Loader 0.20.1+ · Fabric API 0.165.0+26.3
- Built and verified in CI via GitHub Actions

### Installation

1. Install Fabric Loader for Minecraft 26.3
2. Install Fabric API
3. Drop `bettertrimtooltips263-*.jar` into your `mods` folder

Client-side only.

### Original mod & credit

This is an unofficial modern port by **Itamio** of **Better Trim
Tooltips** by **Andrew6rant (Andrew Grant)** — the original (4M+
downloads on Modrinth, last updated for 1.20.2) has not been updated
to 26.3.

- Original mod: https://modrinth.com/mod/better-trim-tooltips
- Original source: https://github.com/Andrew6rant/Better-Trim-Tooltips
- License: MIT (Copyright © 2023 Andrew Grant) — see `LICENSE`

All credit for the original concept, design, and logic goes to
Andrew6rant. This port exists to bring it to Minecraft 26.3. See `NOTICE`.
