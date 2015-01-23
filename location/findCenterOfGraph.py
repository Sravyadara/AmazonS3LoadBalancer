import userDefTypes

def findCenterOfGraph(G):
    "This finds the node which represents the 'center' of the graph"

    # the center of the graph is the vertex with the minimum of all maximum distances for that vertex
    #vList contains the index of the IDCs for which we need to find the center of
    Du = G.Du

    minValue = float("inf")
    for x in range(0,Du.__len__()):
        row = Du[x]
        maxValue = 0
        for y in range(0,row.__len__()):
            if (row[y] > maxValue) & (x != y) & (row[y]!=float("inf")):
                maxValue = row[y]
        if maxValue < minValue:
            minValue = maxValue
            minx = x
    center = G.Vu[minx]
    return (center,minx,minValue)

