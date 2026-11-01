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

## Running it

Requires JDK 11+ (IntelliJ IDEA recommended).

1. Turn off the normal **GPU** plugin (and 117 HD). The plugin declares it conflicts with them, so
   RuneLite will do this for you when you enable it.
2. From this folder run:

   ```
   ./gradlew run
   ```

   (Windows: `gradlew.bat run`). This launches a development RuneLite client with the plugin
   loaded. To log in with a Jagex account in a development client follow
   https://github.com/runelite/runelite/wiki/Using-Jagex-Accounts
3. Enable **Low Res GPU** in the plugin list and open its settings.

## Publishing to the Plugin Hub

Push this folder to a public GitHub repository and open a pull request on
https://github.com/runelite/plugin-hub adding a file `plugins/low-res-gpu` that contains:

```
repository=https://github.com/<you>/low-res-gpu.git
commit=<full commit hash>
```

The hub reviewers look closely at renderer plugins; expect questions.

## Keeping up with RuneLite

The renderer is copied from RuneLite's GPU plugin (commit `7715a1d`, 30 Oct 2026). When RuneLite
changes its rendering API, re-copy the upstream `gpu` package and re-apply the changes. They are
confined to `LowResGpuPlugin.java` (`computeSceneResolution`, `initFbo`, `shutdownFbo`, the
FBO/viewport setup in `preSceneDrawToplevel`, the projection line, and `blitSceneFbo`) plus the new
items at the top of `LowResGpuConfig.java` and the two enums in `config/`.

## License

BSD 2-Clause, same as RuneLite. See `LICENSE`.
