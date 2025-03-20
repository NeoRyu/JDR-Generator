import {Button} from '@/components/ui/button'
import {Dialog, DialogContent, DialogFooter, DialogTrigger,} from '@/components/ui/dialog'
import {ScrollArea} from '@/components/ui/scroll-area'
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue,} from '@/components/ui/select'
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow,} from '@/components/ui/table'
import {Textarea} from '@/components/ui/textarea'
import {useCreateCharacter} from '@/services/create-character.service'
import {useListCharacters} from '@/services/list-characters.service'
import dayjs from 'dayjs'
import {Eye} from 'lucide-react'
import {useState} from 'react'

export function Home() {
  const { data, refetch } = useListCharacters()
  const { mutate, isLoading } = useCreateCharacter()

  const [isOpen, setIsOpen] = useState(false)

  const [gameSystem, setGameSystem] = useState('')
  const [race, setRace] = useState('')
  const [characterClass, setCharacterClass] = useState('')
  const [description, setDescription] = useState('')

  const handleGenerate = () => {
    if (!gameSystem || !race || !characterClass) {
      return alert('Remplissez les champs')
    }

    mutate(
      { gameSystem, class: characterClass, race, description },
      {
        onSuccess: () => {
          refetch()

          setGameSystem('')
          setRace('')
          setCharacterClass('')
          setDescription('')

          setIsOpen(false)
        },
      },
    )
  }

  return (
    <div className="h-screen flex flex-col px-4">
      <header className="h-16 flex items-center">
        <h2 className="text-muted-foreground">Personnages JDR</h2>
      </header>

      <main className="flex flex-col">
        <div className="flex justify-end">
          <Dialog open={isOpen}>
            <DialogTrigger asChild>
              <Button onClick={() => setIsOpen(true)}>Nouveau personnage</Button>
            </DialogTrigger>

            <DialogContent>
              <Select value={gameSystem} onValueChange={setGameSystem}>
                <SelectTrigger>
                  <SelectValue placeholder="Sélectionnez le système de jeu" />
                </SelectTrigger>

                <SelectContent>
                  <SelectItem value="Dungeons & Dragons">
                    Dungeons & Dragons
                  </SelectItem>
                  <SelectItem value="Pathfinder">Pathfinder</SelectItem>
                  <SelectItem value="World of Darkness">
                    World of Darkness
                  </SelectItem>
                  <SelectItem value="Call of Cthulhu">
                    Call of Cthulhu
                  </SelectItem>
                  <SelectItem value="Warhammer Fantasy Roleplay">
                    Warhammer Fantasy Roleplay
                  </SelectItem>
                  <SelectItem value="Shadowrun">Shadowrun</SelectItem>
                  <SelectItem value="GURPS">GURPS</SelectItem>
                  <SelectItem value="Fate">Fate</SelectItem>
                </SelectContent>
              </Select>

              <Select value={race} onValueChange={setRace}>
                <SelectTrigger>
                  <SelectValue placeholder="Sélectionnez la race du personnage" />
                </SelectTrigger>

                <SelectContent>
                  <SelectItem value="Human">Humain</SelectItem>
                  <SelectItem value="Elf">Elfe</SelectItem>
                  <SelectItem value="Dwarf">Nain</SelectItem>
                  <SelectItem value="Halfling">Halfling</SelectItem>
                  <SelectItem value="Orc">Orc</SelectItem>
                  <SelectItem value="Gnome">Gnome</SelectItem>
                  <SelectItem value="Tiefling">Tiefling</SelectItem>
                  <SelectItem value="Dragonborn">Draconien</SelectItem>
                  <SelectItem value="Half-Elf">Demi-Elfe</SelectItem>
                  <SelectItem value="Half-Orc">Demi-Orc</SelectItem>
                  <SelectItem value="Tabaxi">Tabaxi</SelectItem>
                  <SelectItem value="Firbolg">Firbolg</SelectItem>
                  <SelectItem value="Aarakocra">Aarakocra</SelectItem>
                  <SelectItem value="Kenku">Kenku</SelectItem>
                  <SelectItem value="Goliath">Goliath</SelectItem>
                  <SelectItem value="Aasimar">Aasimar</SelectItem>
                </SelectContent>
              </Select>

              <Select value={characterClass} onValueChange={setCharacterClass}>
                <SelectTrigger>
                  <SelectValue placeholder="Sélectionnez la classe du personnage" />
                </SelectTrigger>

                <SelectContent>
                  <SelectItem value="Warrior">Guerrier</SelectItem>
                  <SelectItem value="Wizard">Mage</SelectItem>
                  <SelectItem value="Rogue">Voleur</SelectItem>
                  <SelectItem value="Cleric">Clerc</SelectItem>
                  <SelectItem value="Paladin">Paladin</SelectItem>
                  <SelectItem value="Bard">Barde</SelectItem>
                  <SelectItem value="Druid">Druide</SelectItem>
                  <SelectItem value="Sorcerer">Sorcier</SelectItem>
                  <SelectItem value="Monk">Moine</SelectItem>
                  <SelectItem value="Ranger">Ranger</SelectItem>
                  <SelectItem value="Barbarian">Barbare</SelectItem>
                  <SelectItem value="Warlock">Magicien</SelectItem>
                  <SelectItem value="Fighter">Combattant</SelectItem>
                  <SelectItem value="Necromancer">Nécromant</SelectItem>
                  <SelectItem value="Alchemist">Alchimiste</SelectItem>
                  <SelectItem value="Summoner">Invocateur</SelectItem>
                </SelectContent>
              </Select>

              <Textarea
                placeholder="Description"
                value={description}
                onChange={(event) => setDescription(event.target.value)}
              />

              <DialogFooter>
                <Button
                  type="button"
                  disabled={isLoading}
                  onClick={handleGenerate}
                >
                  {isLoading ? 'Génération...' : 'Générer !'}
                </Button>
              </DialogFooter>
            </DialogContent>
          </Dialog>
        </div>

        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Nom</TableHead>
              <TableHead>Lieu de naissance</TableHead>
              <TableHead>État matrimonial</TableHead>
              <TableHead>A des enfants</TableHead>
              <TableHead>Age</TableHead>
              <TableHead>Creation</TableHead>
              <TableHead />
            </TableRow>
          </TableHeader>

          <TableBody>
            {data?.map((character: any) => (
              <TableRow key={character.id}>
                <TableCell>{character.name}</TableCell>
                <TableCell>{character.birthPlace}</TableCell>
                <TableCell>{character.maritalStatus}</TableCell>
                <TableCell>{character.children}</TableCell>
                <TableCell>{character.age}</TableCell>
                <TableCell>
                  {dayjs(character.createdAt).format('DD/MM/YYYY')}
                </TableCell>
                <TableCell>
                  <Dialog>
                    <DialogTrigger asChild>
                      <Button variant="outline">
                        <Eye />
                      </Button>
                    </DialogTrigger>

                    <DialogContent>
                      <div className="flex flex-col">
                        <img
                          className="rounded shadow w-56 h-56 m-auto"
                          src={character.image}
                          alt={character.name}
                        />

                        <ScrollArea className="h-64 mt-6">
                          <Table>
                            <TableBody>
                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Nom
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.name}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Age
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.age}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Lieu de naissance
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.birthPlace}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  État matrimonial
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.maritalStatus}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Profession
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.profession}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Education
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.education}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Description Physique
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.physicalDescription}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Préférence vestimentaire
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.clothingPreferences}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Routine journalière
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.dailyRoutine}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Loisirs
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.hobbies}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Nourriture favorite
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.favoriteFood}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Peurs
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.fears}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Objectifs
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.goal}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Obstacle majeur
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.biggestObstacle}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Attitude envers les autres
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.attitudeTowardsPeople}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Attitude envers le groupe
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.attitudeTowardsGroups}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Vision du monde
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.attitudeTowardsWorld}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Changement souhaité de soi
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.changeInSelf}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Changement souhaité dans le monde
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.changeInWorld}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Histoire de son enfance
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.childhoodStory}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Signe distinctif
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.distinctiveTrait}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Parents en vie
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.parentsAlive}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Sentiments par rapport aux parents
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.feelingsAboutParents}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Détails sur les parents
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.detailsAboutParents}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Fraternité
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.siblings}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Amis d'enfance
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.youthFriends}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Animal de compagnie
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.pet}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Entreprise idéale
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.idealCompany}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Type de partenaire romantique
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.typeOfLover}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Raison de l'objectif
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.reasonForGoal}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Raison de la profession
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.reasonForProfession}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Préférences de travail
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.workPreferences}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Climat
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.climate}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Lieu de résidence
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.residenceLocation}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Raison de la résidence
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.reasonForResidence}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Problèmes communs
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.commonProblems}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Surmonter les obstacles
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.overcomingObstacle}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Plan en cas de succès
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.planIfSuccessful}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Plan en cas d'échec
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.planIfFailed}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Description de la personne
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.selfDescription}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Activités de loisir
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.leisureActivities}
                                </TableCell>
                              </TableRow>

                              <TableRow>
                                <TableCell className="text-muted-foreground">
                                  Histoire conjugale
                                </TableCell>
                                <TableCell className="flex justify-end">
                                  {character.conjugalHistory}
                                </TableCell>
                              </TableRow>
                            </TableBody>
                          </Table>
                        </ScrollArea>
                      </div>
                    </DialogContent>
                  </Dialog>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </main>
    </div>
  )
}
