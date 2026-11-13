# iOS Liquid Glass Bottom Bars — Implementation Plan for Lunaris Singularity

**Target repository:** [Lunaris-AOSP/packages_apps_Singularity](https://github.com/Lunaris-AOSP/packages_apps_Singularity)  
**Feature:** iOS-style floating Liquid Glass bottom/tab bars in supported Android apps  
**Document status:** Research and implementation plan — not yet implemented  
**Last prepared:** 2026-11-13

---

## 1. Objective

Implement a ROM-integrated, opt-in feature that gives supported applications an iOS-inspired **floating, translucent Liquid Glass bottom navigation/tab bar**.

The intended result may include:
- A floating, rounded/pill-shaped bar rather than a flat bar attached to the bottom edge.
- Background blur behind the bar, where supported by the graphics stack.
- Subtle translucency/tint, edge highlights and a lens-like appearance.
- Correct spacing/insets so content can scroll behind the floating bar without being obscured.
- A settings switch and an app allowlist/denylist so the feature can be enabled selectively.
- A safe fallback when an app, device, GPU, or Android branch cannot support the full effect.

**Important scope distinction:** `packages_apps_Singularity` is the ROM customization/settings application. It can expose and manage the feature, but it cannot by itself make arbitrary third-party apps draw a floating glass tab bar. The rendering and app-window behavior may require coordinated changes in framework code, native rendering, and individual apps.

---

## 2. Research references

The links below are leads found during repository/commit searches. Treat them as source material to inspect, not as proof that every commit is required or directly cherry-pickable into the target branch. Verify the complete diff, parent commit, branch, licensing, and dependencies before porting.

### A. Settings / preference UI

1. **RisingOS preference switch**  
   [Give apps' bottom bars iOS Liquid Glass [2/2]](https://github.com/RisingOS-Revived/android_packages_apps_Personalizations/commit/2991991727c973a0204eff46a3623f3862932188)  
   Repository: `RisingOS-Revived/android_packages_apps_Personalizations`  
   Known purpose: adds a preference for `penguin_glass_tab_bars`, titled **Enable BottomBar UI Revamp**, with a summary describing iOS-style bottom-bar UI for Google apps. This is a settings/UI change, not the whole rendering implementation.

2. **PenguinOS Liquid Glass settings page**  
   [Add Liquid Glass](https://github.com/Project-PenguinOS/penguin_packages_apps_PenguinLaboratory/commit/320bf9acd3ba73a1558e88b53dbe80f5013e47d1)  
   Repository: `Project-PenguinOS/penguin_packages_apps_PenguinLaboratory`

3. **PenguinOS glass tab-bar setting**  
   [Add glass tab bars to the Liquid Glass page](https://github.com/Project-PenguinOS/penguin_packages_apps_PenguinLaboratory/commit/45be0b33ab4adcb050e42c3ef8eda20b1fdb9bd9)  
   Repository: `Project-PenguinOS/penguin_packages_apps_PenguinLaboratory`  
   Search lead for the preference key `penguin_glass_tab_bars`.

4. **App selection / eligibility handling**  
   [Offer only apps that showed a tab bar, and see every app](https://github.com/Project-PenguinOS/penguin_packages_apps_PenguinLaboratory/commit/fdd0d6db144a58fddf5379e36e40fce10de3ea1b)  
   Repository: `Project-PenguinOS/penguin_packages_apps_PenguinLaboratory`  
   Inspect this for how supported apps and exceptions are represented.

### B. Framework and native rendering

5. **Framework-base implementation lead**  
   [Liquid Glass bottom-bar framework change](https://github.com/penguinos-shadedark/frameworks_base/commit/03472af45c6d496f74b2c6c1298de63700e95204)  
   Repository: `penguinos-shadedark/frameworks_base`  
   Inspect the full diff first. Establish exactly which classes/APIs are modified, whether it is a complete implementation or a follow-up, and what system properties/settings it expects. Do not cherry-pick blindly.

6. **PenguinOS native rendering — initial Liquid Glass blur regions**  
   [Draw blur regions as Liquid Glass](https://github.com/Project-PenguinOS/android_frameworks_native/commit/e156cab70e55c98e1e14428d7f23b784e153bcaa)  
   Repository: `Project-PenguinOS/android_frameworks_native`

7. **PenguinOS native rendering — edge/lens tuning**  
   [Lens-curved Liquid Glass edges with debug tuning](https://github.com/Project-PenguinOS/android_frameworks_native/commit/f8240ec43bf74680a44691a6bb358e85567b0e60)  
   Repository: `Project-PenguinOS/android_frameworks_native`

8. **PenguinOS native rendering — performance-oriented slab changes**  
   [Cheaper Liquid Glass slabs](https://github.com/Project-PenguinOS/android_frameworks_native/commit/0c647bba069ce0db6de0a855bc0f5329e8dde68b)  
   Repository: `Project-PenguinOS/android_frameworks_native`

9. **PenguinOS native rendering — DeX/window blur follow-up**  
   [Keep Liquid Glass on for DeX screens and make window blurs vivid](https://github.com/Project-PenguinOS/android_frameworks_native/commit/e274301f8139df988c0a4715bd1388e904e4aa58)  
   Repository: `Project-PenguinOS/android_frameworks_native`

10. **RisingOS native rendering counterpart**  
    [renderengine: Draw blur regions as Liquid Glass](https://github.com/RisingOS-Revived/android_frameworks_native/commit/48c0ad18b3e2b20a3719f8f0935646830a658aaa)  
    Repository: `RisingOS-Revived/android_frameworks_native`  
    This is a general RenderEngine blur/rendering change. It is not independently confirmed to be the only native dependency of the bottom-bar feature.

### C. Example app-specific implementation (Dialer)

11. [Use Liquid Glass for the tab bar and search button](https://github.com/Project-PenguinOS/penguin_packages_apps_Dialer/commit/9043499fc846a10d0657baebd8716df403484e84)

12. [Float the Liquid Glass tab bar in its own window](https://github.com/Project-PenguinOS/penguin_packages_apps_Dialer/commit/ce048fc7f1bbc8b6c0b013a5822e00bba70e2c2b)

13. [Make the Liquid Glass bars crystal clear](https://github.com/Project-PenguinOS/penguin_packages_apps_Dialer/commit/15ff389ea9a65648155f4f46143595d80bf4a93c)

14. [Drop the glass tint and bend the fallback blur](https://github.com/Project-PenguinOS/penguin_packages_apps_Dialer/commit/5ad73aaf35ec1564a083edb8dda119d0c72f4de4)

15. [Bend the glass bars and colour swiped cards](https://github.com/Project-PenguinOS/penguin_packages_apps_Dialer/commit/12164bed524daf5c0778de9c00fc05e9f39cf30d)

These Dialer changes are examples of app-level work. They should not be treated as universal framework patches; they may rely on app-specific layouts, window configuration, or APIs.

### D. Target repository

- [Lunaris-AOSP/packages_apps_Singularity](https://github.com/Lunaris-AOSP/packages_apps_Singularity)
- The current repository search did not reveal an existing implementation keyed to `penguin_glass_tab_bars` or a clearly matching Liquid Glass feature. The current source should still be inspected locally before deciding where to add the setting.

---

## 3. Proposed architecture

Use a layered implementation. Keep the preference name and storage contract consistent across Settings and framework code, but prefer a Lunaris-owned key unless compatibility with an existing system-side implementation requires the PenguinOS key.

### Layer 1 — Singularity settings UI

Responsibilities:
- Add a **Liquid Glass** category or place the toggle under the existing relevant customization category.
- Add a switch: **Liquid Glass bottom bars**.
- Add explanatory summary: *Use a floating, translucent glass-style bottom navigation bar in supported apps.*
- Optionally provide an app list to enable/disable the effect per package.
- Show a clear note that only supported applications are affected.
- Persist changes using the storage mechanism expected by the framework implementation.

Investigate before coding:
- Existing preference classes and XML/Kotlin/Java patterns in Singularity.
- Existing `SystemSetting*Preference`, `SecureSetting*Preference`, or `GlobalSetting*Preference` classes and which settings namespace is appropriate.
- Existing app-picker/list preference implementations that can be reused.
- Search indexing, string resources, translations, backup/default behavior, and whether a reboot/SystemUI restart is required.

Do not assume the setting should be a `System`, `Secure`, or `Global` setting until the framework consumer and access permissions are known. Avoid introducing a custom property unless the consumer genuinely requires one.

### Layer 2 — Feature state and app eligibility

Define and document:
- Master enabled/disabled state.
- Allowlist or denylist semantics.
- Default behavior when the list is empty or missing.
- How package names are validated and stored.
- Behavior after app updates, app removal, user/profile switching, and multi-user use.
- Whether unsupported apps are hidden from the picker or displayed as unsupported.

Prefer a stable, documented setting contract. Do not leave an undocumented dependency on a PenguinOS-only preference key.

### Layer 3 — Framework integration

The framework implementation must be inspected to determine the exact interception point. The goal is to:
- Detect eligible bottom navigation/tab-bar surfaces without changing unrelated UI.
- Apply floating positioning, rounded geometry, correct elevation/layering, and insets.
- Preserve taps, accessibility, focus, gesture navigation, animations, and scrolling.
- Avoid applying the effect to keyboards, dialogs, transient sheets, system bars, or unrelated bottom-aligned controls.
- Respect per-app opt-outs and the global switch.

Do not implement fragile global view matching based only on text labels or guessed resource IDs. If the upstream implementation uses specific view identifiers, package checks, window flags, or APIs, document their limitations and port them with tests.

### Layer 4 — Native blur / Liquid Glass rendering

Inspect the relevant `frameworks/native` changes against the exact Android branch used by the ROM:
- Blur-region creation and submission.
- RenderEngine / SurfaceFlinger path and supported blur APIs.
- Glass tint, edge highlight, radius, opacity, and lens distortion.
- Fallback behavior on unsupported devices or configurations.
- GPU cost, memory/bandwidth, frame pacing, and battery impact.

Do not assume that the RenderEngine commits are mandatory for a basic translucent floating bar. First determine whether the framework/app can use existing Android blur APIs; add native changes only when required for the intended visual result.

### Layer 5 — App-level adaptation

A framework-wide change may not transform arbitrary applications automatically. Some apps may need their own UI changes to:
- Put the tab bar in a separate floating window/container.
- Provide a transparent background behind the bar.
- Allow content to draw/scroll behind the bar.
- Configure window blur and insets correctly.
- Keep navigation items clickable and accessible.

Use the Dialer commits as a reference implementation, not as a patch to apply to every app. Build a small compatibility list of apps that have been tested.

---

## 4. Recommended implementation phases

### Phase 0 — Pin down the build baseline

- [ ] Record Android version and exact ROM branch.
- [ ] Record current revisions of `packages_apps_Singularity`, `frameworks_base`, `frameworks_native`, SystemUI, and relevant target apps.
- [ ] Create a feature branch and save clean baseline build/test results.
- [ ] Confirm upstream licenses and retain required copyright/license headers.

**Exit criteria:** the target branch and the relevant upstream diffs are known.

### Phase 1 — Audit upstream changes and dependencies

- [ ] Read the full diff and parent history of every linked commit.
- [ ] Follow the `frameworks_base` change to any companion commits, referenced APIs, properties, resources, and settings keys.
- [ ] Follow the native rendering commits in chronological order; identify prerequisites and later fixes.
- [ ] Find the original “1/2” companion commit for the RisingOS Personalizations change, if it exists.
- [ ] Search other modified app repositories for the same APIs/setting contract.
- [ ] Create a dependency table: commit, repository, purpose, required/optional, target-branch compatibility, conflicts.

**Exit criteria:** no required dependency is inferred from a title alone.

### Phase 2 — Add the Singularity control

- [ ] Add the master switch and strings.
- [ ] Wire it to the agreed setting contract.
- [ ] Add an app picker only if the framework implementation supports per-app eligibility.
- [ ] Add defaults, summaries, search indexing, and translations as appropriate.
- [ ] Verify persistence across process restart and reboot.
- [ ] Verify disabling the switch restores the original UI.

**Exit criteria:** settings build cleanly and the switch changes the expected stored state.

### Phase 3 — Port framework behavior

- [ ] Port only the required framework changes.
- [ ] Resolve API/resource differences against the ROM branch.
- [ ] Add guards for supported app/window types and user setting state.
- [ ] Preserve navigation, insets, touch targets, accessibility, and window lifecycle.
- [ ] Add a kill switch/default-off path for debugging.
- [ ] Build affected framework targets before starting a full ROM build.

**Exit criteria:** supported test app renders correctly and unsupported apps remain unchanged.

### Phase 4 — Port native rendering only if needed

- [ ] Add the minimum compatible RenderEngine/SurfaceFlinger changes.
- [ ] Resolve native API and build-system differences.
- [ ] Confirm behavior with blur disabled, enabled, and unsupported fallback.
- [ ] Measure GPU/frame-time impact before enabling by default.

**Exit criteria:** stable rendering, correct fallback, and no unacceptable performance regression.

### Phase 5 — Pilot apps

Start with one app, preferably the upstream Dialer implementation if it matches the ROM's app version:
- [ ] Verify floating-bar layout and transparent background.
- [ ] Verify scrolling content is visible behind the bar.
- [ ] Verify portrait/landscape, gesture navigation, three-button navigation if supported, display cutouts, and font/display scaling.
- [ ] Verify tap targets, TalkBack/accessibility, RTL, dark/light theme, and animation transitions.
- [ ] Test keyboard opening, dialogs, bottom sheets, and app switching.
- [ ] Add more apps only after the pilot is stable.

**Exit criteria:** no regression in navigation, app lifecycle, or accessibility.

### Phase 6 — Performance, compatibility, and release

- [ ] Test on target hardware/GPU and at multiple refresh rates.
- [ ] Test thermal behavior, battery, memory pressure, and frame pacing.
- [ ] Test multi-user/profile behavior and package allowlist updates.
- [ ] Test feature disabled and unsupported-device fallback.
- [ ] Record known limitations and supported apps.
- [ ] Ensure the feature can be disabled without reflashing.

**Exit criteria:** stable, reversible feature with documented compatibility.

---

## 5. Acceptance checklist

- [ ] Master toggle exists in Singularity and persists.
- [ ] Toggle off fully restores the normal app UI.
- [ ] Only explicitly supported/eligible apps are affected.
- [ ] Bar has correct floating geometry and clipping.
- [ ] Blur is visible only where supported and does not produce black/transparent rendering artifacts.
- [ ] Content is not permanently hidden behind the bar.
- [ ] Gestures, taps, scrolling, keyboard, and dialogs behave normally.
- [ ] Accessibility labels and touch targets remain usable.
- [ ] RTL, landscape, cutouts, and display scaling are handled.
- [ ] No SystemUI/framework crashes, watchdogs, or recurring SurfaceFlinger/RenderEngine errors.
- [ ] Performance and battery impact are measured.
- [ ] Feature can be disabled safely.

---

## 6. Risks and safeguards

| Risk | Safeguard |
|---|---|
| Upstream commits target a different Android branch | Port semantically; do not cherry-pick blindly |
| Framework patch changes unrelated bottom-aligned views | Gate by app/window eligibility and test broad UI cases |
| Native blur API differs or is unavailable | Add capability checks and a graceful translucent fallback |
| Blur is expensive on target GPU | Benchmark; make effect optional and tune radius/quality |
| App picker lists incompatible apps | Maintain eligibility logic and explicit unsupported states |
| Setting key mismatch between Singularity and framework | Define one documented contract and test read/write behavior |
| App update changes tab-bar structure | Test package versions and avoid brittle view heuristics |
| Accessibility/touch behavior regresses | Validate TalkBack, hit targets, focus, and gestures |

---

## 7. Commands for local audit

Run these from a checkout of the ROM source. Replace paths/branch names as appropriate.

```bash
# Inspect the target settings repository
cd packages/apps/Singularity
git status --short
git log --oneline -20
grep -RniE 'SystemSetting|SecureSetting|GlobalSetting|Preference' src res/xml | head -200

# Search the ROM source tree for existing blur / Liquid Glass work
cd ../../..
grep -RniE 'penguin_glass_tab_bars|Liquid Glass|liquid_glass|BackgroundBlur|blur.*region' \
  frameworks/base frameworks/native packages/apps 2>/dev/null | head -300

# Fetch and inspect upstream commit metadata/diffs in separate checkouts
git show --stat --oneline <commit-sha>
git show --format=fuller --find-renames <commit-sha>
git log --oneline --decorate --all --grep='Liquid Glass'
```

For each upstream commit, record:
- Full SHA and parent SHA.
- Repository and source branch.
- Files changed.
- Referenced setting/property/API names.
- Companion commits and prerequisites.
- Whether the change is required, optional, or app-specific.
- Build/test outcome on the target branch.

---

## 8. Porting policy

1. **Do not cherry-pick the list as a batch.** First create a dependency map.
2. **Do not assume `penguin_glass_tab_bars` is the right final key.** It is an upstream identifier. Use it only if the ported framework expects it; otherwise use a documented Lunaris key and update all consumers together.
3. **Do not copy Dialer-specific changes globally.** Use them to understand app-level requirements.
4. **Do not enable the feature by default until compatibility and performance are verified.**
5. **Preserve existing Singularity code and behavior.** Add the feature in a focused patch series with reversible commits.
6. **Keep licensing and attribution.** Review each upstream repository's license and retain required notices.

---

## 9. Immediate next actions

1. Pin down the ROM's exact Android branch and current framework revisions.
2. Inspect the full diff for the `frameworks_base` implementation lead.
3. Trace the companion commit(s) for the RisingOS `[1/2]` change.
4. Build a commit dependency table before writing code.
5. Implement the Singularity preference only after confirming the framework-side setting contract.
6. Port and test the framework/native pieces in separate, reviewable commits.

**Bottom line:** start in `packages_apps_Singularity` for the UI and user control, but plan the feature as a cross-repository change. The setting alone will not create Liquid Glass bars; the framework, rendering path, and possibly individual apps must cooperate.
