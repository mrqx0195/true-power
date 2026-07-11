# True POWER

An addon mod for **[SlashBlade:Resharped](https://modrinth.com/mod/slashblade-resharped)** that significantly overhauls
its combat mechanics.

# Overview

It's evident that SlashBlade: Resharped's combat system extensively replicates Vergil's combat system from *Devil May
Cry 5*.  
However, it still has numerous unsatisfying aspects, including awkward controls and problematic key-press detection.  
This mod comprehensively replaces and rewrites Resharped's combat system to pursue more "DMC-like" combat—True POWER.

*Recommended to play with **[Shoulder Surfing Reloaded](https://modrinth.com/mod/shoulder-surfing-reloaded)**.*

# Feature List

*(Items marked with ⚙ are configurable via file)*

## Concentration Rank Related

- ⚙ *(Configurable Rank Requirement)* Automatically enter **Powered** state at high Concentration Ranks. *(Default:
  Requires SSS Rank)*
- ⚙ *(Configurable Point Values)* Reduces Rank Points gained from attacks.
- ⚙ *(Configurable Point Values)* Gain temporary Concentration Rank upon killing enemies. Performing a sheathing
  animation converts it into permanent Rank Points.
- Rank Points can only increase once per **Game Tick** via dealing damage.
- High Concentration Ranks grant damage bonuses when wielding a SlashBlade; low Ranks impose penalties.
- Increased point thresholds for achieving S/SS/SSS Ranks, but natural Rank decay will now cause Rank level decreases.

## Combo Related

- ⚙ *(Toggleable)* Player movement is restricted during certain combos. *(However, in some cases, jumping or Trick
  abilities can forcibly cancel the restriction.)* *(v1.2.0+ can be modified via datapack, though only mod-built-in and
  KubeJS-provided data is supported.)*
- Repeating the same combo prevents Rank Point gain for a duration.
- ⚙ *(Damage Configurable)* Sneak + (Back → Forward) + Left/Right Click *(Detection similar to DMC5)* executes **Void
  Slash**. *(Note: This differs from the SA version of Void Slash!)*
- The Back→Forward Void Slash deals two hits when in Powered state and pulls in nearby enemies upon sheathing.
- ⚙ *(Target Count Configurable)* While Powered, the SA **Judgement Cut** can strike two additional nearby targets.
- The final strike of **Combo C** affixes a **Mirage Blade** to the target. This sword explodes and launches the target
  into the air upon sheathing. *(Upgraded to 6 swords in **Powered** state)*
- While **Powered**, **Rapid Slash** affixes a **Mirage Blade** to all hit enemies upon ending. These swords explode and
  launch targets into the air during sheathing.
- Adjusted damage values for most combos, with additional handling optimizations for specific skills.
- ⚙ *(v1.2.0+) (Toggleable, enabled by default)* Adds a **Stun Value** system. When enabled, the vanilla SlashBlade
  stagger system is disabled:
    - All creatures have an independent Stun Value gauge. Taking certain combo attacks accumulates Stun Value; when
      full, they enter a **Stun State**: electric spark particles appear on their head, most actions are disabled,
      targets are lost, lasting approximately 6 seconds, after which all Stun Value is restored.
    - All creatures possess the `truepower:stun_resistance` attribute *(default: 1.0)*. Maximum Stun Value is the
      product of this attribute and the creature's base health.
    - Attacks in Powered state accumulate Stun Value **20%** more efficiently; Powered creatures receive **25%** less
      Stun Value accumulation.
    - Certain attacks directly trigger a brief Stun effect.
- ⚙ *(v1.2.0+) (Configurable)* While holding a SlashBlade, collision with other entities becomes **Solid Collision**;
  while airborne or during certain combos it becomes **Ignore Collision**.

## Key Bindings

- **Trick Up** binding changed to: Press **Special Move** key *without* directional input.
- **Heavy Rain Swords** binding changed to: Sneak + (Back → Forward) + Press **Summon Mirage Blade** key *(Detection
  similar to DMC5)*.
- **Trick Down** binding changed to: Sneak + (Back → Forward) + Press **Special Move** key *(Detection similar to DMC5)*.
    - *(v1.2.0+)* Holding down the **Special Move** key will automatically trigger consecutive Tricks.
- *(v1.2.0+)* Input windows for all Back → Forward inputs have been significantly expanded.
- *(v1.2.0+)* While locking on, the mouse wheel can be used to cycle through lock-on targets.

## Mirage Blade Related

- ⚙ *(v1.1.7+) (Toggleable, multiplier adjustable)* Mirage Blade damage is significantly reduced, but is now affected
  by the player's base attack power.

## Miscellaneous

- Wielding a SlashBlade grants additional damage bonuses while in the Powered state.
- Players cannot enter sneak/swim states while holding a SlashBlade.
- While using combos, players cannot switch held items *(prevents certain issues)*.
- ⚙ *(Enabled by default since v1.2.0) (Toggleable)* Certain combos propel the player forward *(as in DMC5)*.

## Client Config

- ⚙ *(v1.1.7+) (Toggleable, lock value adjustable)* Field of View (FOV) will be locked to a fixed value while holding
  a SlashBlade.
- ⚙ *(Multiplier Adjustable)* Camera sensitivity increases by 4× while locking on.

# Compatibility Features

## *(v1.2.0+)* Shoulder Surfing Reloaded

*(The following are special behaviors when using Shoulder Surfing Reloaded)*

- While holding a SlashBlade, automatic camera rotation is disabled in most situations.
- Movement input handling has been modified: for combo key press detection, inputs are rotated according to the player
  entity's facing direction *(as in DMC5)* *(e.g., if the player faces right, pressing the **Move Right** key will
  trigger Rapid Slash)* *(does not affect actual player movement)*.
- While locking on, the entity at the crosshair is prioritized over the entity the player entity is facing.

# License

This mod is open-source under the GPL v3.0 License.

You may include it in your modpacks/servers as long as you **comply with the license terms**.
