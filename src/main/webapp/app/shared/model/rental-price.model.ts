import { IRental } from '@/shared/model/rental.model';

import { Season } from '@/shared/model/enumerations/season.model';
export interface IRentalPrice {
  id?: number;
  value?: number;
  season?: Season;
  rental?: IRental;
}

export class RentalPrice implements IRentalPrice {
  constructor(public id?: number, public value?: number, public season?: Season, public rental?: IRental) {}
}
