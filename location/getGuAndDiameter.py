import math
import userDefTypes
import findCenterOfGraph


def findVertexIdx(Vu,vertex):
    for x in range(0,Vu.__len__()):
        if(Vu[x] == vertex):
            return x

def addDists(resGraph,Gm):
    resGraph.Du = []
    resGraph.Eu = []
    resGraph.Du = [0] * resGraph.Vu.__len__()
    resGraph.Eu = [0] * resGraph.Vu.__len__()
    for x in range(0,resGraph.Vu.__len__()):
        x_idx =  findVertexIdx(Gm.Vu,resGraph.Vu[x])
        resGraph.Du[x] = [0] * resGraph.Vu.__len__()
        resGraph.Eu[x] = [0] * resGraph.Vu.__len__()
        for y in range(0,resGraph.Vu.__len__()):
            y_idx = findVertexIdx(Gm.Vu,resGraph.Vu[y])
            resGraph.Du[x][y] = Gm.Du[x_idx][y_idx]
            resGraph.Eu[x][y] = Gm.Eu[x_idx][y_idx]

def calcDistance(u, Gu, Gm):
    assert isinstance(Gu, userDefTypes.graph)
    assert isinstance(Gm, userDefTypes.graph)
    (center,minx,minValue) = findCenterOfGraph.findCenterOfGraph(Gu)
    x1 = []
    x2 = []
    for x in range(0,Gm.Vu.__len__()):
        curr = Gm.Vu[x]
        if(curr == u):
            x1.append(x)
        if(curr == center):
            x2.append(x)
    if( (x1.__len__() ==0 ) | (x2.__len__() == 0)):
        return float("inf")
    else:
        if( (x1.__len__() > 1  ) | (x2.__len__() > 1)):
            print "Error!"
            exit(0)
        return( Gm.Du[x1[0]][x2[0]])

def connectedVertices(v_idx,Gm):
    assert isinstance(Gm, userDefTypes.graph)
    vertexList    = []
    vertexIdxList = []
    numVertices = Gm.Eu[v_idx].__len__()
    for i in range(0,numVertices):
        if( (Gm.Eu[v_idx][i] == 1) & (i != v_idx)):
            vertexList.append(Gm.Vu[i])
            vertexIdxList.append(i)

    return (vertexList,vertexIdxList)

def getGuAndDiameter(v_idx,Gm,U,mu):
    assert isinstance(Gm, userDefTypes.graph)
    assert isinstance(U, userDefTypes.userRequest)
    resGraph = userDefTypes.graph();
    resGraph.Vu = []
    resGraph.Cu = []
    resGraph.Eu = []
    resGraph.Du = []
    ceilKbyMu  = math.ceil(U.K/mu)
    floorKbyMu = math.floor(U.K/mu)

    #line 1
    resGraph.Vu.append(Gm.Vu[v_idx])
    resGraph.Cu.append(min([ Gm.Cu[v_idx] , ceilKbyMu ]))
    addDists(resGraph,Gm)

    #line 2
    (Q,Qidx) = connectedVertices(v_idx,Gm)

    #line 3
    diameter = 0

    #line 4
    last = U.K - resGraph.Cu[0]

    if last == 0:
        return (resGraph,0,0)
    #line 5
    for z in range(1,mu+1):
        #line 6
        tmp_dia = float("inf")
        tmp_v = []
        tmp_v_idx = -1
        #line 7
        for u_idx in range(0,Q.__len__()):
            distUandGu = calcDistance ( Q[u_idx], resGraph, Gm)
            vertex_idx = findVertexIdx(Gm.Vu,Q[u_idx])
            #line 8
            if ((  (z<mu) & (Gm.Cu[vertex_idx] >= floorKbyMu) ) | ( (z==mu) & (Gm.Cu[vertex_idx] >= last) )) & (distUandGu < tmp_dia):
                #line 9
                tmp_dia = distUandGu
                #line 10
                tmp_v = Q[u_idx]
                tmp_v_idx = vertex_idx

        if tmp_v_idx!=-1:   #tmp_v_idx is not empty
            #line13
            resGraph.Vu.append(tmp_v)

            cVal = min([ceilKbyMu, Gm.Cu[tmp_v_idx], last])
            resGraph.Cu.append(cVal)
            addDists(resGraph,Gm)
            #line 14
            (moreQ,moreQidx) = connectedVertices(tmp_v_idx,Gm)
            Q = list(set(Q+moreQ))
            Q = list(set.difference(* list( [set(Q),set(resGraph.Vu)])))
            diameter = max([diameter,tmp_dia])
        else:
             #line15
            tmp_dia = 0
            diameter = max([diameter,tmp_dia])
            cVal = 0

        #line16
        last = last - cVal
        if last == 0:
            break

    return (resGraph,diameter,last)


# "main" function
# M = userDefTypes.graph()
# U = userDefTypes.userRequest()
#
# ## case 3
# M.Vu = [ 1,2,3,4]
# M.Eu = [[1,1,0,0],[1,1,0,0],[0,0,1,1],[0,0,1,1]]
# M.Du = [[0,3,float("inf"),float("inf")],[3,0,float("inf"),float("inf")],[float("inf"),float("inf"),0,3],[float("inf"),float("inf"),3,0]]
# M.Cu = [3,3,3,3]
#
# U.Pu = [0,0]
# U.K  = 4
# U.mu = 2

# ## case 2
# M.Vu = [ 1,2]
# M.Eu = [[1,0],[0,1]]
# M.Du = [[0,float("inf")],[float("inf"),0]]
# M.Cu = [3,3]
#
# U.Pu = [0,0]
# U.K  = 2
# U.mu = 1

# ## case 1
# M.Vu = [ 1]
# M.Eu = [[1]]
# M.Du = [[0]]
# M.Cu = [3]
#
# U.Pu = [0,0]
# U.K  = 1
# U.mu = 1

# M.Vu = [ 1,2,3]
# M.Eu = [[1,1,0],[1,1,1],[0,1,1]]
# M.Du = [[0,5,float("inf")],[5,0,3],[float("inf"),3,0]]
# M.Cu = [3,3,3]
#
# U.Pu = [0,0]
# U.K  = 2
# U.mu = 1


#(resGraph, diameter, last) = getGuAndDiameter(0,M,U,U.mu)
#print resGraph.Vu, resGraph.Cu, resGraph.Eu, resGraph.Du
# (resGraph, diameter, last) = getGuAndDiameter(0,M,U,U.mu)
# print resGraph.Vu, resGraph.Cu, resGraph.Eu, resGraph.Du
# print diameter, last