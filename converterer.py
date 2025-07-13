import json

# Open
f = open('anim_to_read.json')

data = json.load(f)
conversion = {"Body" : "body", "RightArm" : "right_arm", "LeftArm" : "left_arm", "RightHand" : "right_hand", "LeftHand" : "left_hand", "Head" : "head", "RightLeg" : "right_leg", "LeftLeg" : "left_leg",}
convertEasing = {"linear" : "LINEAR", "easeInSine" : "SINE_IN", "easeOutSine" : "SINE_OUT", "easeInOutSine" : "SINE_IN_OUT", "easeInQuad" : "QUAD_IN", "easeOutQuad" : "QUAD_OUT", "easeInOutQuad" : "QUAD_IN_OUT", "easeInQuart" : "QUARTIC_IN", "easeOutQuart" : "QUARTIC_OUT", "easeInOutQuart" : "QUARTIC_IN_OUT", "easeInQuint" : "QUINTIC_IN", "easeOutQuint" : "QUINTIC_OUT", "easeInOutQuint" : "QUINTIC_IN_OUT", "easeInBack" : "BACK_IN", "easeOutBack" : "BACK_OUT", "easeInOutBack" : "BACK_IN_OUT", "easeInExpo" : "EXPO_IN", "easeOutExpo" : "EXPO_OUT", "easeInOutExpo" : "EXPO_IN_OUT"}
uberlist = []

def getMolang(value):
    return "new MoLangVec3fSupplier((math, query) -> " + str(value[2][0]) +", (math, query) -> " + str(value[2][1]) +", (math, query) -> " + str(value[2][2]) + ")"

def getMolangDeg(value):
    return "new MoLangVec3fSupplier((math, query) -> " + str(value[2][0]) +", (math, query) -> " + str(value[2][1]) +", (math, query) -> " + str(value[2][2]) + ", true)"

def isMolang(value):
    if 'math' in str(value[2][0]) or 'query' in str(value[2][0]):
        return True
    if 'math' in str(value[2][1]) or 'query' in str(value[2][1]):
        return True
    if 'math' in str(value[2][2]) or 'query' in str(value[2][2]):
        return True
    return False

# the son of the J
# future arathain here, no fucking idea what this is supposed to mean.
for i in data['animations']:
    animList = []
    for k in data['animations'][i]['bones']:
        frames = {}
        if 'rotation' in data['animations'][i]['bones'][k]:
            frameTimes = []
            easings = []
            vecs = []
            if len(data['animations'][i]['bones'][k]['rotation']) == 1:
                frameTimes.append(0.0);
                easings.append("linear");
                vecs.append(data['animations'][i]['bones'][k]['rotation']['vector']);
            else:
                for keyframe in data['animations'][i]['bones'][k]['rotation']:
                    frameTimes.append(float(keyframe))
                    if 'easing' in data['animations'][i]['bones'][k]['rotation'][keyframe]:
                        easings.append(data['animations'][i]['bones'][k]['rotation'][keyframe]['easing'])
                    else:
                        easings.append("linear")
                    vecs.append(data['animations'][i]['bones'][k]['rotation'][keyframe]['vector'])
            easings.pop(0)
            easings.append("linear")
            for soup in range(len(easings)):
                frames[frameTimes[soup]] = [("rot", easings[soup], vecs[soup])]

        if 'position' in data['animations'][i]['bones'][k]:
            frameTimes = []
            easings = []
            vecs = []
            if len(data['animations'][i]['bones'][k]['position']) == 1:
                frameTimes.append(0.0);
                easings.append("linear");
                vecs.append(data['animations'][i]['bones'][k]['position']['vector']);
            else:
                for keyframe in data['animations'][i]['bones'][k]['position']:
                    frameTimes.append(float(keyframe))
                    if 'easing' in data['animations'][i]['bones'][k]['position'][keyframe]:
                        easings.append(data['animations'][i]['bones'][k]['position'][keyframe]['easing'])
                    else:
                        easings.append("linear")
                    vecs.append(data['animations'][i]['bones'][k]['position'][keyframe]['vector'])
            easings.pop(0)
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
            if len(data['animations'][i]['bones'][k]['scale']) == 1:
                frameTimes.append(0.0);
                easings.append("linear");
                vecs.append(data['animations'][i]['bones'][k]['scale']['vector']);
            else:
                for keyframe in data['animations'][i]['bones'][k]['scale']:
                    frameTimes.append(float(keyframe))
                    if 'easing' in data['animations'][i]['bones'][k]['scale'][keyframe]:
                        easings.append(data['animations'][i]['bones'][k]['scale'][keyframe]['easing'])
                    else:
                        easings.append("linear")
                vecs.append(data['animations'][i]['bones'][k]['scale'][keyframe]['vector'])
            easings.pop(0)
            easings.append("linear")
            for soup in range(len(easings)):
                if frameTimes[soup] in frames.keys():
                    frames[frameTimes[soup]].append(("scale", easings[soup], vecs[soup]))
                else:
                    frames[frameTimes[soup]] = [("scale", easings[soup], vecs[soup])]
        animList.append((conversion[k] if k in conversion else k, (frames)))

    uberlist.append((animList, int(data['animations'][i]['animation_length']*20), str(i)))
# data extracted, you can now pray
f.close()

with open('export.txt', 'a') as f:
    for anim in uberlist:
        for jointAnim in anim[0]:
            for frame in range(len(jointAnim[1])):
                key = list(jointAnim[1].keys())[frame]
                value = list(jointAnim[1].values())[frame]
                rot = "ProperVec3fSupplier.ZERO"
                pos = "ProperVec3fSupplier.ZERO"
                scale = "ProperVec3fSupplier.ZERO"
                easing = "LINEAR"
                for val in value:
                    match val[0]:
                        case "pos":
                            if isMolang(val):
                                pos = getMolang(val)
                            else:
                                pos = "new ProperVec3fSupplier(" + str(val[2][0]) +"f, " + str(val[2][1]) +"f, " + str(val[2][2]) + "f)"
                            easing = val[1]
                        case "scale":
                            if isMolang(val):
                                scale = getMolang(val)
                            else:
                                scale = "new ProperVec3fSupplier(" + str(val[2][0]) +"f, " + str(val[2][1]) +"f, " + str(val[2][2]) + "f)"
                            easing = val[1]
                        case "rot":
                            if isMolang(val):
                                rot = getMolangDeg(val)
                            else:
                                rot = "deg(" + str(round(val[2][0], 2)) +"f, " + str(round(val[2][1], 2)) +"f, " + str(round(val[2][2], 2)) + "f)"
                            easing = val[1]
                f.write("cache.add(new Keyframe(Easing." + (convertEasing[easing] if easing in convertEasing else easing) + ", " + pos + ", " + rot + ", " + scale + ", " + "" + ("new ProperVec3fSupplier(0, -12, 0), " if jointAnim[0] == "body" else "ProperVec3fSupplier.ZERO, ") + str(round(key*20, 2 if frame != len(jointAnim[1])-1 else 0))+ "f));\n")
            f.write(anim[2].upper() + ".put(\"" + jointAnim[0] + "\", new ArrayList<>(cache));\n")
            f.write("cache.clear();\n")
        f.write("Animations.put(id(\"" + anim[2] + "\"), () -> new Animation(" + str(anim[1]) + ", " + anim[2].upper() +"));\n")
    f.close()


