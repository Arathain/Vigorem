import json

# Opening JSON file
f = open('anim_to_read.json')

# returns JSON object as 
# a dictionary
data = json.load(f)
conversion = {"Body" : "body", "RightArm" : "right_arm", "LeftArm" : "left_arm", "Head" : "head", "RightLeg" : "right_leg", "LeftLeg" : "left_leg",}
uberlist = []
# Iterating through the json
# list
for i in data['animations']:
    animList = []
    for k in data['animations'][i]['bones']:
        frames = {}
        if 'rotation' in data['animations'][i]['bones'][k]:
            frameTimes = []
            easings = []
            vecs = []
            for keyframe in data['animations'][i]['bones'][k]['rotation']:
                frameTimes.append(float(keyframe))
                if 'easing' in data['animations'][i]['bones'][k]['rotation'][keyframe]:
                    easings.append(data['animations'][i]['bones'][k]['rotation'][keyframe]['easing'])
                vecs.append(data['animations'][i]['bones'][k]['rotation'][keyframe]['vector'])
            easings.append("linear")
            for soup in range(len(easings)):
                frames[frameTimes[soup]] = [("rot", easings[soup], vecs[soup])]

        if 'position' in data['animations'][i]['bones'][k]:
            frameTimes = []
            easings = []
            vecs = []
            for keyframe in data['animations'][i]['bones'][k]['position']:
                frameTimes.append(float(keyframe))
                if 'easing' in data['animations'][i]['bones'][k]['position'][keyframe]:
                    easings.append(data['animations'][i]['bones'][k]['position'][keyframe]['easing'])
                vecs.append(data['animations'][i]['bones'][k]['position'][keyframe]['vector'])
            easings.append("linear")
            for soup in range(len(easings)):
                if frameTimes[soup] in frames.keys():
                    frames[frameTimes[soup]].append(("pos", easings[soup], vecs[soup]))
                else:
                    frames[frameTimes[soup]] = [("pos", easings[soup], vecs[soup])]

        if 'scale' in data['animations'][i]['bones'][k]:
            frameTimes = []
            easings = []
            vecs = []
            for keyframe in data['animations'][i]['bones'][k]['scale']:
                frameTimes.append(float(keyframe))
                if 'easing' in data['animations'][i]['bones'][k]['scale'][keyframe]:
                    easings.append(data['animations'][i]['bones'][k]['scale'][keyframe]['easing'])
                vecs.append(data['animations'][i]['bones'][k]['scale'][keyframe]['vector'])
            easings.append("linear")
            for soup in range(len(easings)):
                if frameTimes[soup] in frames.keys():
                    frames[frameTimes[soup]].append(("scale", easings[soup], vecs[soup]))
                else:
                    frames[frameTimes[soup]] = [("scale", easings[soup], vecs[soup])]
        animList.append((conversion[k], (frames)))
        # if k in conversion:
        #    print(conversion[k])
        # print(frames)
    uberlist.append((animList, int(data['animations'][i]['animation_length'])*20, str(i)))
# Closing file
f.close()

with open('export.txt', 'a') as f:
    f.write("import static arathain.vigorem.init.Animations.deg;\n\n\n")
    f.write("List<Keyframe> cache = new ArrayList<>();\n")
    for anim in uberlist:
        for jointAnim in anim[0]:
            for frame in range(len(jointAnim[1])):
                key = list(jointAnim[1].keys())[frame]
                value = list(jointAnim[1].values())[frame]
                rot = "Vec3f.ZERO"
                pos = "Vec3f.ZERO"
                scale = "Vec3f.ZERO"
                easing = "Easing.LINEAR"
                for val in value:
                    match val[0]:
                        case "pos":
                            pos = "new Vec3f(" + str(val[2][0]) +", " + str(val[2][1]) +", " + str(val[2][2]) + ")"
                            easing = "Easing." + val[1]
                        case "scale":
                            scale = "new Vec3f(" + str(val[2][0]) +", " + str(val[2][1]) +", " + str(val[2][2]) + ")"
                            easing = "Easing." + val[1]
                        case "rot":
                            rot = "deg(" + str(val[2][0]) +", " + str(val[2][1]) +", " + str(val[2][2]) + ")"
                            easing = "Easing." + val[1]
                f.write("cache.add(new Keyframe(" + easing + ", " + scale + ", " + pos + ", " + rot + ", " + "" + ("new Vec3f(0, -12, 0), " if jointAnim[0] == "body" else "Vec3f.ZERO, ") + str(key) + "));\n")
            f.write(anim[2].upper() + ".put(\"" + jointAnim[0] + "\", new ArrayList<>(cache));\n")
            f.write("cache.clear();\n")
        f.write("Animations.put(id(\"" + anim[2] + "\"), () -> new Animation(" + str(anim[1]) + ", " + anim[2].upper() +");")
    f.close()
