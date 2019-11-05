import Data.List

type Node = Integer
type Edge = (Integer, Integer)
type Graph = [Edge]
type Path = [Node]

g :: Graph
g = [(1,2), (1,3), (2,3), (2,4), (3,4)]
h :: Graph
h = [(1,2), (1,3), (2,1), (3,2), (4,4)]

nodes :: Graph -> [Node]
nodes g = sort (nub ((fst (unzip g)) ++ (snd (unzip g))))

adjacent :: Node -> Graph -> [Node]
adjacent v (x:xs) = if ((fst x) == v)
                    then (snd x) : (adjacent v xs)
                    else (adjacent v xs)
adjacent v [] = []

detach :: Node -> Graph -> Graph
detach v (x:xs) = if (((fst x) == v) || ((snd x) == v))
                    then detach v xs
                    else x : (detach v xs)
detach v [] = []

paths :: Node -> Node -> Graph -> [Path]
paths v1 v2 g = if (v1 == v2)
                then [[v2]]
                else removeCycles [[v1] ++ path | edge <- g, (fst edge) == v1, path <- paths (snd edge) v2 [e | e <- g, e /= edge]]

removeCycles :: [Path] -> [Path]
removeCycles (x:xs) = if (x == (nub x))
                      then x : (removeCycles xs)
                      else (removeCycles xs)
removeCycles [] = []
