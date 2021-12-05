import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';
import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import AlertService from '@/shared/alert/alert.service';

import ReservationService from '@/entities/reservation/reservation.service';
import { IReservation } from '@/shared/model/reservation.model';

import RentalPriceService from '@/entities/rental-price/rental-price.service';
import { IRentalPrice } from '@/shared/model/rental-price.model';

import { IRental, Rental } from '@/shared/model/rental.model';
import RentalService from './rental.service';

const validations: any = {
  rental: {
    beginingInstant: {
      required,
    },
    endingInstant: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class RentalUpdate extends Vue {
  @Inject('rentalService') private rentalService: () => RentalService;
  @Inject('alertService') private alertService: () => AlertService;

  public rental: IRental = new Rental();

  @Inject('reservationService') private reservationService: () => ReservationService;

  public reservations: IReservation[] = [];

  @Inject('rentalPriceService') private rentalPriceService: () => RentalPriceService;

  public rentalPrices: IRentalPrice[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.rentalId) {
        vm.retrieveRental(to.params.rentalId);
      }
      vm.initRelationships();
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }

  public save(): void {
    this.isSaving = true;
    if (this.rental.id) {
      this.rentalService()
        .update(this.rental)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('isyYachtingApp.rental.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      this.rentalService()
        .create(this.rental)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('isyYachtingApp.rental.created', { param: param.id });
          this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    }
  }

  public convertDateTimeFromServer(date: Date): string {
    if (date && dayjs(date).isValid()) {
      return dayjs(date).format(DATE_TIME_LONG_FORMAT);
    }
    return null;
  }

  public updateInstantField(field, event) {
    if (event.target.value) {
      this.rental[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.rental[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.rental[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.rental[field] = null;
    }
  }

  public retrieveRental(rentalId): void {
    this.rentalService()
      .find(rentalId)
      .then(res => {
        res.beginingInstant = new Date(res.beginingInstant);
        res.endingInstant = new Date(res.endingInstant);
        this.rental = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.reservationService()
      .retrieve()
      .then(res => {
        this.reservations = res.data;
      });
    this.rentalPriceService()
      .retrieve()
      .then(res => {
        this.rentalPrices = res.data;
      });
  }
}
