# Vigorem
- A unique approach to player animations, prioritising server-client synchronisation and offering additional extensions to the base API.

## Usage Guide
- Please refer to the "test" folder of the project, as well as the Animations class, to see example usage of the mod. For converting geckolib animations to code, please see the 'converterer' python script.
- Note on compatibility: first-person animations work natively with vanilla armour, as well as all implementations of the FRAPI ArmorRenderer, provided it calls the "renderPart" method inside of its render method; if you do not do so, please manually check for parent model visibility in your mod's renderer in order to ensure compatibility.
