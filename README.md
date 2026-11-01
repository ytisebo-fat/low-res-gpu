# Low Res GPU

A RuneLite plugin that is RuneLite's own GPU plugin, but it draws the 3D world at a **low internal
resolution** (for example 800×600) and stretches it to fill your screen with **nearest-neighbour**
scaling. You keep everything the GPU plugin gives you (draw distance, fog, unlocked FPS, smooth
animations, anti-aliasing, anisotropic filtering) and fullscreen play, but with a chunky, blocky,
retro look.

## How it works

The stock GPU plugin already renders the 3D scene into an offscreen buffer and copies it to the
window. This plugin:

1. sizes that buffer to the low resolution you choose instead of your screen resolution,
2. maps the game's 3D viewport into that smaller buffer (with a small projection correction so
   nothing shifts in fixed mode), and
3. copies it to the screen with `GL_NEAREST` (blocky) or `GL_LINEAR` (soft) filtering.

The 3D cost scales with pixel count, so 800×600 is also much lighter on your GPU than 4K.

## Settings (top of the plugin's config panel)

| Setting | What it does |
|---|---|
| Resolution mode | **Target height**: render N pixels tall, width follows your screen (square pixels). **Pixel size**: each rendered pixel becomes an N×N block (crispest, perfectly even). **Fixed resolution**: exactly W×H, e.g. 800×600 (pixels go non-square on widescreen, geometry is still correct). **Off**: behaves like the normal GPU plugin. |
| Target height | Default 600. On a 1920×1080 screen that is 1067×600. |
| Pixel size | Default 3. On 1920×1080 that is 640×360, on 2560×1440 it's 854×480. |
| Fixed width / height | Default 800×600. |
| Upscale filter | Nearest (blocky, default) or Linear (blurry). |

All the regular GPU settings (draw distance, AA, fog, FPS...) follow below.

**Tip for the most even blocks:** use *Pixel size* mode. With *Target height* the scale factor is
usually not a whole number (1080 / 600 = 1.8), so some rows of blocks are 2 pixels and some are 1.

**Tip for a fully retro look including the interface:** this plugin only lowers the resolution
of the 3D world; the UI (inventory, chat, minimap) stays sharp. To make the UI chunky too, also
enable RuneLite's **Stretched Mode** plugin, turn on *Resizable scaling* (e.g. 200%), and set this
plugin's *UI scaling mode* to *Nearest Neighbor*.

Anti-aliasing works but softens the blocks; set it to *Disabled* for the hardest pixel edges.

## Status

This plugin is **not on the Plugin Hub**: RuneLite currently doesn't accept new GPU plugins
(see runelite/plugin-hub#19899). You can run it yourself as a development build, described below.
A "render scale" option for the official GPU plugin has been suggested to the RuneLite developers.

## Running it

### What you need
- **Java 21 (JDK).** Newer versions (24 and up) do **not** work with this project's build tool and
  fail with *"Unsupported class file major version"*. Java 21 can be installed next to any other
  Java version you have, for example from https://adoptium.net/temurin/releases/?version=21
  (choose the JDK `.msi`) or from Oracle.
- This repository: click the green **Code** button > **Download ZIP**, and unzip it.

### Windows
Double-click **`run.bat`**. It finds Java 21 in the usual install folders, uses it just for this
project, and starts a RuneLite development client with the plugin loaded. The first start
downloads RuneLite, which takes a minute or two. If Java 21 is installed somewhere unusual, set the
environment variable `JAVA21_HOME` to its folder first.

### macOS / Linux
In a terminal, inside this folder:

```
JAVA_HOME=/path/to/your/jdk-21 ./gradlew run
```

### Logging in with a Jagex account
A development client can't be started from the Jagex launcher, so it has to reuse your login.
Follow RuneLite's official guide: https://github.com/runelite/runelite/wiki/Using-Jagex-Accounts

In short: in **RuneLite (configure)** add `--insecure-write-credentials` to *Client arguments*,
start RuneLite once from the Jagex launcher, then start this plugin's client.

**Be careful:** this saves your login session to `.runelite/credentials.properties` in your user
folder. Never share that file. When you're done, delete it and remove the argument again.

### Turning it on
Enable **Low Res GPU** in the plugin list. RuneLite turns the normal **GPU** plugin (and 117 HD)
off automatically, since only one renderer can run. The low-res settings are at the top of the
plugin's config.

## Keeping up with RuneLite

The renderer is copied from RuneLite's GPU plugin (commit `7715a1d`, 30 Oct 2026). When RuneLite
changes its rendering API, re-copy the upstream `gpu` package and re-apply the changes. They are
confined to `LowResGpuPlugin.java` (`computeSceneResolution`, `initFbo`, `shutdownFbo`, the
FBO/viewport setup in `preSceneDrawToplevel`, the projection line, and `blitSceneFbo`) plus the new
items at the top of `LowResGpuConfig.java` and the two enums in `config/`.

## Credits

The renderer is RuneLite's GPU plugin, written by Adam and the RuneLite contributors
(https://github.com/runelite/runelite, `runelite-client/src/main/java/net/runelite/client/plugins/gpu`).
This plugin only adds the low resolution render target and upscaling on top of their work.

## License

BSD 2-Clause, same as RuneLite. The original copyright notices are kept in every file that came
from RuneLite. See `LICENSE`.
