# Release procedure

Releases are promoted only from an owner-accepted, reviewed commit on `main`.
The accepted candidate identity is recorded in `provenance/release.json`.

## Clean gate

Use Java 21, Gradle 9.6.1, the exact sibling BlueMap checkout, and the exact
local Ender IO artifact:

```bash
gradle --no-daemon \
  -PenderIoJar=/absolute/path/enderio-8.2.11-beta.jar \
  -PreleaseTag=v0.1.0-alpha.3 \
  clean check build generatePomFileForAddonPublication \
  generateMetadataFileForAddonPublication verifyPublicationArtifacts \
  verifyReleaseCandidate
```

Inspect the production and sources JARs. Reject NeoForge metadata, nested
JARs, upstream classes/assets, gallery output, tests, research data, or
unexpanded metadata.

## Runtime and publication

Run the deterministic gallery against that exact JAR, open the intended
BlueMap link for the required lightweight sanity check, and obtain explicit
owner acceptance. All accepted non-manifest entry bytes are frozen; only the
recorded final-version manifest transition is permitted.

Before tagging, merge the release pull request. Create and push an annotated
`v<addon_version>` tag at that reviewed `main` commit. The release workflow
reproduces every accepted byte, creates a draft prerelease, uploads and
attests the assets, publishes the Maven package, verifies the draft assets,
and only then makes the prerelease public.

Never reuse or move a release tag. A failed prepublication run may be resumed
with the workflow's exact immutable tag input while its GitHub release remains
a draft. Publication deploys nothing to the Minecraft server.
