## Why

The current image generation prompt is long and highly structured (TITLE, CONTENT/BULLET POINTS, VISUAL DESIGN INSTRUCTIONS, ADDITIONAL STYLE, IMPORTANT REQUIREMENTS). This increases token usage and complexity without clear benefit, since the outline already produces a single English description per slide. Simplifying to a single-sentence format reduces prompt size, keeps behavior explicit, and makes it easier to tune style (with or without GlobalSettings.visualStyle).

## What Changes

- Replace the multi-block image prompt with a single-sentence format.
- **With** `visualStyle` configured: intro phrase + visualStyle text + slide description (English) + closing phrase.
- **Without** `visualStyle`: intro phrase + default style phrase ("Modern minimalist style with clean typography") + slide description + closing phrase.
- Remove explicit injection of title and content into the prompt; the slide description from the outline is the only variable content.
- Update backend: `OpenAIImageService.buildDecoratedPrompt` and `ContentProcessingServiceImpl.buildImagePrompt` to build the new format.

## Capabilities

### New Capabilities

(No new capabilities.)

### Modified Capabilities

- **image-generation**: Add requirement defining the simplified prompt format (with/without visualStyle). The prompt sent to DALL-E 3 SHALL follow this format instead of the previous structured blocks.

## Impact

- **Backend**: [OpenAIImageService](backend/src/main/java/com/ayg/presentaciones/service/OpenAIImageService.java) (`buildDecoratedPrompt`), [ContentProcessingServiceImpl](backend/src/main/java/com/ayg/presentaciones/service/ContentProcessingServiceImpl.java) (`buildImagePrompt`). No API or interface signature changes; only the constructed string format changes.
- **Spec**: [openspec/specs/image-generation/spec.md](openspec/specs/image-generation/spec.md) — new requirement and scenarios for prompt format.
