# Changelog

## 0.1.0-alpha.1 - 2026-08-21

- Add an exact-artifact BlueMap renderer for Ender IO's painted redstone block.
- Read only the persisted top-level `Paint` resource ID and reuse admitted
  propertyless opaque full-cube face materials and UVs.
- Preserve BlueMap's position-stable model-variant selection.
- Fall back atomically for malformed, recursive, tinted, translucent,
  transformed, animated, weighted, multipart, or non-cube targets.
- Add focused Java 21 tests, exact artifact and resource validation, a
  deterministic six-cell staging gallery, and release-byte gates.
