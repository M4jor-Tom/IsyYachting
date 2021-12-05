import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import DayService from '@/entities/day/day.service';
import { IDay } from '@/shared/model/day.model';

import ClientService from '@/entities/client/client.service';
import { IClient } from '@/shared/model/client.model';

import RentalService from '@/entities/rental/rental.service';
import { IRental } from '@/shared/model/rental.model';

import { IReservation, Reservation } from '@/shared/model/reservation.model';
import ReservationService from './reservation.service';

const validations: any = {
  reservation: {
    day: {
      required,
    },
    client: {
      required,
    },
    rental: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class ReservationUpdate extends Vue {
  @Inject('reservationService') private reservationService: () => ReservationService;
  @Inject('alertService') private alertService: () => AlertService;

  public reservation: IReservation = new Reservation();

  @Inject('dayService') private dayService: () => DayService;

  public days: IDay[] = [];

  @Inject('clientService') private clientService: () => ClientService;

  public clients: IClient[] = [];

  @Inject('rentalService') private rentalService: () => RentalService;

  public rentals: IRental[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.reservationId) {
        vm.retrieveReservation(to.params.reservationId);
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
    if (this.reservation.id) {
      this.reservationService()
        .update(this.reservation)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('isyYachtingApp.reservation.updated', { param: param.id });
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
      this.reservationService()
        .create(this.reservation)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('isyYachtingApp.reservation.created', { param: param.id });
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

  public retrieveReservation(reservationId): void {
    this.reservationService()
      .find(reservationId)
      .then(res => {
        this.reservation = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.dayService()
      .retrieve()
      .then(res => {
        this.days = res.data;
      });
    this.clientService()
      .retrieve()
      .then(res => {
        this.clients = res.data;
      });
    this.rentalService()
      .retrieve()
      .then(res => {
        this.rentals = res.data;
      });
  }
}
