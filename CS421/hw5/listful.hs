seal :: Ord a => [a] -> [a] -> [a]
seal (x:xs) (y:ys) = if x < y
                      then x:(seal xs (y:ys))
                      else y:(seal (x:xs) ys)
seal [] xs = xs
seal xs [] = xs

isSublist :: Eq a => [a] -> [a] -> Bool
isSublist (xs) (ys) = if (length xs) <= (length ys)
                      then if  xs == take (length xs) ys
                            then True
                            else isSublist xs (drop 1 ys)
                      else False

combinator :: [t] -> [t] -> [[t]]
combinator (x:xs) (y:ys) = [[x,y] | x<-(x:xs), y<-(y:ys)]
combinator [] xs = []
combinator xs [] = []

mightOfPythagorus :: Int -> [(Int, Int, Int)]
mightOfPythagorus x = [(a, b, c) | c<-[1..x], b<-[1..c], a<-[1..b], a^2+b^2==c^2]
