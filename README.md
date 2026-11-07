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

### Resolution mode: how big are the pixels?

All modes answer the same question in a different way. They're listed from most to least "original".

| Mode | You choose | Best for | Keep in mind |
|---|---|---|---|
| **Match interface (original)** *(default)* | nothing | the authentic look: the 3D world uses exactly the same pixels as the interface | needs RuneLite's **Stretched Mode** plugin, otherwise it renders at full resolution (the plugin tells you in the chat box) |
| **Pixel size** | block size, e.g. 3 = every pixel becomes 3×3 | crisp, perfectly even blocks | the resolution depends on your screen (3 on 1440p = 480p, on 1080p = 360p) |
| **Target height** | how many pixels tall, e.g. 480 | a specific "480p" look on any screen | usually not a whole-number scale, so some blocks are a pixel bigger than others |
| **Fixed resolution** | exact width × height, e.g. 800×600 | an exact retro resolution | on widescreen monitors the pixels become wider than they are tall |
| **Off** | nothing | comparing with the normal GPU plugin | — |

### Other settings

| Setting | What it does |
|---|---|
| Target height / Pixel size / Fixed width & height | The numbers for the matching mode above. Each name says which mode it belongs to; the other modes ignore it. |
| Upscale filter | Nearest (blocky, default) or Linear (blurry). |
| Snap to pixels | On by default. Snaps every polygon corner to a whole pixel of the low resolution image, like the original software renderer did with its whole-number (fixed-point) maths. Polygons jump from pixel to pixel as the camera moves, for an authentic wobbly retro look. Turn it off for smooth movement. |

All the regular GPU settings (draw distance, AA, fog, FPS...) follow below.

**Tip:** for the original fixed-point look, also leave the GPU setting **Remove vertex snapping** off.
That one controls the slightly jittery model animations of the original game.

## Recommended setups

### The original 2007 game, full screen
Exactly the original 765×503 game, pixel for pixel, filling your screen.

1. In-game settings: **Game client layout: Fixed – Classic layout**
2. Turn on RuneLite's **Stretched Mode** plugin and tick **Keep aspect ratio**, otherwise the game is stretched wide. Leave **Integer scaling** off so the game fills your screen.
3. Low Res GPU: **Resolution mode: Match interface (original)**
4. Low Res GPU: **UI scaling mode: Nearest Neighbor**, **Snap to pixels** on, **Remove vertex snapping** off

### Modern full screen, retro pixels
Resizable layout with chunky pixels everywhere.

1. In-game settings: a **Resizable** layout
2. **Stretched Mode** plugin on. Its **Resizable scaling** setting decides the pixel size: higher = bigger pixels. Try a few values.
3. Low Res GPU: **Match interface (original)** and **UI scaling mode: Nearest Neighbor**

The 3D world and the interface now have exactly the same pixel size.

### Retro 3D, sharp interface
Keeps the interface crisp and only makes the 3D world blocky. No Stretched Mode needed.

1. Low Res GPU: **Pixel size** with e.g. 3, or **Target height** with e.g. 480

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

## Changelog

**1.1**
- New default resolution mode: *Match interface (original)*. The 3D world uses exactly the interface's pixels; with the Fixed layout and Stretched Mode that is the original 765×503 game.
- New: *Snap to pixels*, which snaps polygon corners to the low resolution pixel grid for the original fixed-point look (suggested on Reddit).
- Resolution modes are ordered from most to least original, with a clearer explanation in the settings and the README. Settings that only apply to one mode now say so in their name.
- New "Recommended setups" section in the README.

**1.0**
- First release.

## Credits

The renderer is RuneLite's GPU plugin, written by Adam and the RuneLite contributors
(https://github.com/runelite/runelite, `runelite-client/src/main/java/net/runelite/client/plugins/gpu`).
This plugin only adds the low resolution render target and upscaling on top of their work.

## License

BSD 2-Clause, same as RuneLite. The original copyright notices are kept in every file that came
from RuneLite. See `LICENSE`.
