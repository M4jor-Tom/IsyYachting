/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import DayComponent from '@/entities/day/day.vue';
import DayClass from '@/entities/day/day.component';
import DayService from '@/entities/day/day.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('Day Management Component', () => {
    let wrapper: Wrapper<DayClass>;
    let comp: DayClass;
    let dayServiceStub: SinonStubbedInstance<DayService>;

    beforeEach(() => {
      dayServiceStub = sinon.createStubInstance<DayService>(DayService);
      dayServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<DayClass>(DayComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          dayService: () => dayServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      dayServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllDays();
      await comp.$nextTick();

      // THEN
      expect(dayServiceStub.retrieve.called).toBeTruthy();
      expect(comp.days[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      dayServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeDay();
      await comp.$nextTick();

      // THEN
      expect(dayServiceStub.delete.called).toBeTruthy();
      expect(dayServiceStub.retrieve.callCount).toEqual(1);
    });
  });
});
