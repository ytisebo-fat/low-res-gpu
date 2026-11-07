/*
 * Copyright (c) 2018, Adam <Adam@sigterm.info>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.lowresgpu;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;
import static com.lowresgpu.LowResGpuPlugin.MAX_DISTANCE;
import static com.lowresgpu.LowResGpuPlugin.MAX_FOG_DEPTH;
import com.lowresgpu.config.AntiAliasingMode;
import com.lowresgpu.config.ColorBlindMode;
import com.lowresgpu.config.LowResMode;
import com.lowresgpu.config.UIScalingMode;
import com.lowresgpu.config.UpscaleFilter;

@ConfigGroup(LowResGpuConfig.GROUP)
public interface LowResGpuConfig extends Config
{
	String GROUP = "lowresgpu";

	// ---- Low resolution settings (new in this plugin) ----

	@ConfigItem(
		keyName = "lowResMode",
		name = "Resolution mode",
		description = "How big the pixels of the 3D world are.<br>"
			+ "<b>Match interface (original)</b>: same resolution as the interface. Needs the Stretched Mode plugin.<br>"
			+ "With the Fixed layout this is exactly the original 765x503 game.<br>"
			+ "<b>Pixel size</b>: every pixel becomes an N x N block. Crisp and even.<br>"
			+ "<b>Target height</b>: render N pixels tall, e.g. 480. Blocks can be slightly uneven.<br>"
			+ "<b>Fixed resolution</b>: exactly width x height, e.g. 800 x 600. Pixels may be non-square.<br>"
			+ "<b>Off</b>: full resolution, like the normal GPU plugin.",
		position = -10
	)
	default LowResMode lowResMode()
	{
		return LowResMode.MATCH_INTERFACE;
	}

	@Range(min = 120, max = 2160)
	@ConfigItem(
		keyName = "targetHeight",
		name = "Target height (Target height mode)",
		description = "Internal render height in pixels.<br>Only used when Resolution mode is 'Target height'.",
		position = -9
	)
	default int targetHeight()
	{
		return 600;
	}

	@Range(min = 1, max = 16)
	@ConfigItem(
		keyName = "pixelSize",
		name = "Pixel size (Pixel size mode)",
		description = "Size of each rendered pixel on screen, e.g. 3 = every pixel becomes a 3x3 block.<br>Only used when Resolution mode is 'Pixel size'.",
		position = -8
	)
	default int pixelSize()
	{
		return 3;
	}

	@Range(min = 160, max = 3840)
	@ConfigItem(
		keyName = "fixedWidth",
		name = "Fixed width (Fixed mode)",
		description = "Internal render width.<br>Only used when Resolution mode is 'Fixed resolution'.",
		position = -7
	)
	default int fixedWidth()
	{
		return 800;
	}

	@Range(min = 120, max = 2160)
	@ConfigItem(
		keyName = "fixedHeight",
		name = "Fixed height (Fixed mode)",
		description = "Internal render height.<br>Only used when Resolution mode is 'Fixed resolution'.",
		position = -6
	)
	default int fixedHeight()
	{
		return 600;
	}

	@ConfigItem(
		keyName = "upscaleFilter",
		name = "Upscale filter",
		description = "How the low resolution image is enlarged to fill the screen.<br>"
			+ "Nearest gives hard, blocky pixels. Linear gives a soft, blurry look.",
		position = -5
	)
	default UpscaleFilter upscaleFilter()
	{
		return UpscaleFilter.NEAREST;
	}

	@ConfigItem(
		keyName = "snapToPixels",
		name = "Snap to pixels",
		description = "Snap every corner of every polygon to a whole pixel of the low resolution image, like the<br>"
			+ "original software renderer, which used whole-number (fixed-point) screen positions.<br>"
			+ "Polygons visibly jump from pixel to pixel when the camera moves, for a wobbly retro look.",
		position = -4
	)
	default boolean snapToPixels()
	{
		return true;
	}

	// ---- Settings below are inherited from RuneLite's GPU plugin ----

	@Range(
		max = MAX_DISTANCE
	)
	@ConfigItem(
		keyName = "drawDistance",
		name = "Draw distance",
		description = "Draw distance.",
		position = 1
	)
	default int drawDistance()
	{
		return 50;
	}

	@ConfigItem(
		keyName = "hideUnrelatedMaps",
		name = "Hide unrelated maps",
		description = "Hide unrelated map areas you shouldn't see.",
		position = 2
	)
	default boolean hideUnrelatedMaps()
	{
		return true;
	}

	@Range(
		max = 5
	)
	@ConfigItem(
		keyName = "expandedMapLoadingChunks",
		name = "Extended map loading",
		description = "Extra map area to load, in 8 tile chunks.",
		position = 1
	)
	default int expandedMapLoadingZones()
	{
		return 3;
	}

	@ConfigItem(
		keyName = "smoothBanding",
		name = "Remove color banding",
		description = "Smooths out the color banding that is present in the CPU renderer.",
		position = 2
	)
	default boolean smoothBanding()
	{
		return true;
	}

	@ConfigItem(
		keyName = "antiAliasingMode",
		name = "Anti aliasing",
		description = "Configures the anti-aliasing mode.",
		position = 3
	)
	default AntiAliasingMode antiAliasingMode()
	{
		return AntiAliasingMode.MSAA_2;
	}

	@ConfigItem(
		keyName = "uiScalingMode",
		name = "UI scaling mode",
		description = "Sampling function to use for the UI in stretched mode.",
		position = 4
	)
	default UIScalingMode uiScalingMode()
	{
		return UIScalingMode.HYBRID;
	}

	@Range(
		max = MAX_FOG_DEPTH
	)
	@ConfigItem(
		keyName = "fogDepth",
		name = "Fog depth",
		description = "Distance from the scene edge the fog starts.",
		position = 5
	)
	default int fogDepth()
	{
		return 0;
	}

	@Range(
		min = 0,
		max = 16
	)
	@ConfigItem(
		keyName = "anisotropicFilteringLevel",
		name = "Anisotropic filtering",
		description = "Configures the anisotropic filtering level.",
		position = 7
	)
	default int anisotropicFilteringLevel()
	{
		return 1;
	}

	@ConfigItem(
		keyName = "colorBlindMode",
		name = "Colorblindness correction",
		description = "Adjusts colors to account for colorblindness.",
		position = 8
	)
	default ColorBlindMode colorBlindMode()
	{
		return ColorBlindMode.NONE;
	}

	@Range(
		min = 0,
		max = 100
	)
	@ConfigItem(
		keyName = "colorBlindIntensity",
		name = "Colorblindness intensity",
		description = "Strength of the colorblindness correction effect.",
		position = 9
	)
	default int colorBlindIntensity()
	{
		return 100;
	}

	@ConfigItem(
		keyName = "brightTextures",
		name = "Bright textures",
		description = "Use old texture lighting method which results in brighter game textures.",
		position = 10
	)
	default boolean brightTextures()
	{
		return false;
	}

	@ConfigItem(
		keyName = "unlockFps",
		name = "Unlock FPS",
		description = "Removes the 50 FPS cap for camera movement.",
		position = 11
	)
	default boolean unlockFps()
	{
		return true;
	}

	enum SyncMode
	{
		OFF,
		ON,
		ADAPTIVE
	}

	@ConfigItem(
		keyName = "vsyncMode",
		name = "Vsync mode",
		description = "Method to synchronize frame rate with refresh rate.",
		position = 12
	)
	default SyncMode syncMode()
	{
		return SyncMode.OFF;
	}

	@ConfigItem(
		keyName = "fpsTarget",
		name = "FPS target",
		description = "Target FPS when 'Unlock FPS' is enabled and 'Vsync mode' is off.",
		position = 13
	)
	@Range(
		min = 1,
		max = 999
	)
	default int fpsTarget()
	{
		return 60;
	}

	@ConfigItem(
		keyName = "removeVertexSnapping",
		name = "Remove vertex snapping",
		description = "Removes vertex snapping from most animations.",
		position = 14
	)
	default boolean removeVertexSnapping()
	{
		return true;
	}

	@ConfigItem(
		keyName = "numThreads",
		name = "Threads",
		description = "Number of render threads to use.",
		position = 20
	)
	@Range(min = 0, max = 15)
	default int numThreads()
	{
		return 3;
	}
}
